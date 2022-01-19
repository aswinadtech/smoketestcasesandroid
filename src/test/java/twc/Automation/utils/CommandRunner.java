package twc.Automation.utils;

//import com.apps_auto.mobile.framework.lib.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;



import twc.Automation.Driver.Drivers;

/**
 * Command line statement execution handler.
 *
 * @author tony
 */
public class CommandRunner extends Drivers{
	private static final int MAX_TIMEOUT_SEC = 30;
	private static final int BLOCKING_TIMEOUT_SEC = 10;
	//private static final MobileAutomationLogger LOGGER = new MobileAutomationLogger(CommandRunner.class);

	/**
	 * Get raw data from output.
	 *
	 * @param isBash     Is it a bash command?
	 * @param command    command to execute
	 * @param isBlocking wait for process before proceeding
	 * @return
	 */
	private static BufferedReader executeCommand(boolean isBash, String command, boolean shouldLog, boolean isBlocking) {
		logStep("input command " + command);
		System.out.println("input command " + command);
		ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));

		Process process = null;
		try {
			if(isBash) {
				process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
			} else {
				process = processBuilder.start();
			}
			if(isBlocking) {
				process.waitFor(BLOCKING_TIMEOUT_SEC, TimeUnit.SECONDS);
			}
		} catch (final IOException | InterruptedException e) {
			logStep(e.getMessage());
			System.out.println(e.getMessage());
		}
		if(process != null) {
			return new BufferedReader(new InputStreamReader(process.getInputStream()));
		}
		return null;
	}

	/**
	 * Execute command with all possible parameters.
	 *
	 * @param command    Command to run. Command will always be logged
	 * @param isBash     Is it a bash command?
	 * @param numOfLines Number of output lines to retrieve. Negatives for unlimited
	 * @param shouldLog  Should log output
	 * @param isBlocking Wait for process before proceeding
	 * @return
	 */
	public static List<String> runCommand(String command, boolean isBash, int numOfLines, boolean shouldLog, boolean isBlocking) {
		BufferedReader reader = executeCommand(isBash, command, shouldLog, isBlocking);
		List<String> output = new ArrayList<>();

		try {
			Thread thread = new Thread(() -> {
				String line = null;
				int count = 0;
				try {
					// if numberOfLines is negative, that means grab all, otherwise limit output limnes
					while ((numOfLines < 0 || count++ < numOfLines) && ((line = reader.readLine()) != null)) {
						if(shouldLog) {
							logStep(line);
							System.out.println(line);
						}
						output.add(line);
					}
					reader.close();
				} catch (IOException e) {
				}
			});
			thread.start();
			thread.join(MAX_TIMEOUT_SEC*1000);
		} catch (InterruptedException e) {
			logStep("Command never completed, force closed reading output");
			System.out.println("Command never completed, force closed reading output");
		}
		return output;
	}

	/**
	 * Execute command with isBash=false, numOfLines=-1, shouldLog=true, isBlocking=false.
	 *
	 * @param command Command to run
	 * @return Output as list.
	 */
	public static List<String> runCommand(String command) {
		return runCommand(command, false, -1, true, false);
	}

	/**
	 * Execute command with isBash=false, numOfLines=-1, shouldLog=true, isBlocking=false.
	 *
	 * @param command Command to run
	 * @return Output as a single string.
	 */
	public static String runCommandOutputAsStr(String command) {
		List<String> output = runCommand(command, false, -1, true, false);
		final StringBuffer retVal = new StringBuffer();
		String nextLine = "";
		output.forEach((str) -> {
			retVal.append(str + "\n");
		});

		return retVal.toString();
	}

	///// ~~~ Bash related shortcuts ~~~ /////
	/**
	 * Execute command using bash.
	 *
	 * @param command    Command to run
	 * @param command    Command to run. Command will always be logged
	 * @param numOfLines Number of output lines to retrieve. Negatives for unlimited
	 * @param shouldLog  Should log output
	 * @param isBlocking Wait for process before proceeding
	 * @return
	 */
	public static List<String> runCommandBash(String command, int numOfLines, boolean shouldLog, boolean isBlocking) {
		return runCommand(command, true, numOfLines, true, isBlocking);
	}

	/**
	 * Execute command using bash with numOfLines=-1, shouldLog=true, isBlocking=false.
	 *
	 * @param command Command to run
	 * @return
	 */
	public static List<String> runCommandBash(String command) {
		return runCommand(command, true, -1, true, false);
	}

	/**
	 * Execute command using bash with numOfLines=-1, shouldLog=true, isBlocking=false.
	 *
	 * @param command Command to run
	 * @return
	 */
	public static String runCommandBashOutputAsStr(String command) {
		List<String> output = runCommand(command, true, -1, true, false);
		final StringBuffer retVal = new StringBuffer();
		String nextLine = "";
		output.forEach((str) -> {
			retVal.append(str + "\n");
		});
		return retVal.toString();
	}
}
