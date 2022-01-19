package twc.Automation.HandleWithCharles;


/**
 * Charles Proxy Exceptions
 *
 * @author tony
 *
 */
@SuppressWarnings("serial")
public class CharlesProxyException extends RuntimeException {
	//protected static final MobileAutomationLogger LOGGER = new MobileAutomationLogger(CharlesConfiguration.class);

	public CharlesProxyException(String message) {
		super(message);
		System.out.println(message);
	}
}
