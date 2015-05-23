package net.sensnet.node.supercommunicatoins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.util.ConnUtils;

public class SuperCommunicationsManager implements Runnable {
	private static final File TARGET_FOLDER = new File("job_cache");
	private static final SimpleDateFormat FILE_FORM = new SimpleDateFormat(
			"yyyy-mm-dd'.synccache'");
	private static final SuperCommunicationsManager instance = new SuperCommunicationsManager();

	private SuperCommunicationsManager() {
		if (!TARGET_FOLDER.exists()) {
			TARGET_FOLDER.mkdirs();
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			File[] fileList = TARGET_FOLDER.listFiles();
			String current = FILE_FORM.format(new Date());
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].getName().equals(current)) {
					synchronized (this) {
						if (!doAction(fileList[i])) {
							break;
						}
					}
				} else {
					if (!doAction(fileList[i])) {
						break;
					}
				}
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static SuperCommunicationsManager getInstance() {
		return instance;
	}

	public synchronized void putJob(HttpSyncAction job) throws IOException {
		File file = new File(TARGET_FOLDER, FILE_FORM.format(new Date()));
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		writer.write(job.getPath());
		writer.write(";");
		writer.write(job.getPostData());
		writer.newLine();
		writer.flush();
		writer.close();
	}

	private boolean doAction(File file) {
		try (BufferedReader read = new BufferedReader(new FileReader(file));) {
			String tmp;
			while ((tmp = read.readLine()) != null) {
				String[] parts = tmp.split(";");
				ConnUtils.postNodeAuthenticatedData(parts[0], parts[1]);
			}
		} catch (IOException | SQLException e) {
			System.err.println("HOUPS! That souldn't happen...");
			e.printStackTrace();
			return false;
		} catch (InvalidNodeAuthException e) {
			return false;
		}
		file.delete();
		System.out.println("Deleted " + file.getName() + ".");
		return true;
	}
}
