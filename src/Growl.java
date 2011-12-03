import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Notify with Growl using AppleScript and Java Script engine.
 *
 * @author Tobias Södergren, Jayway
 */
public class Growl {
    private static final String GROWL_APPLICATION = "GrowlHelperApp";
    private final String applicationName;
    private String[] availableNotifications;
    private String[] enabledNotifications;
    private ScriptEngine appleScriptEngine;

    public Growl(String applicationName, String[] availableNotifications, String[] enabledNotifications) {
    	this.applicationName = applicationName;
        this.availableNotifications = availableNotifications;
        this.enabledNotifications = enabledNotifications;
    }

    public void init() {
    	System.out.println("1");
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        appleScriptEngine = scriptEngineManager.getEngineByName("AppleScript");
        if (appleScriptEngine == null) {
            throw new RuntimeException("No AppleScriptEngine available");
        }

        if (!isGrowlEnabled()) {
        	System.out.println("5");
            throw new RuntimeException("No Growl process was found.");
        }
        System.out.println("6");
    }

    public void registerApplication() {
        String script = script().add("tell application ")
                .quote(GROWL_APPLICATION)
                .nextRow("set the availableList to ")
                .array(availableNotifications)
                .nextRow("set the enabledList to ")
                .array(enabledNotifications)
                .nextRow("register as application ")
                .quote(applicationName)
                .add(" all notifications availableList default notifications enabledList")
                .nextRow("end tell").get();
        executeScript(script);
    }

    public void notify(String notificationName, String title, String message) {
        String script = script().add("tell application ")
                .quote(GROWL_APPLICATION)
                .nextRow("notify with name ").quote(notificationName)
                .add(" title ").quote(title)
                .add(" description ").quote(message)
                .add(" application name ").quote(applicationName)
                .nextRow("end tell").get();
        executeScript(script);
    }

    private boolean isGrowlEnabled() {
    	System.out.println("2");
        String script = script().add("tell application ")
                .quote("System Events")
                .nextRow("set isRunning to count of (every process whose bundle identifier is ")
                .quote("com.Growl."+GROWL_APPLICATION).add(") > 0")
                .nextRow("end tell")
                .get();
        
        /*
         * tell application "System Events"
	set isRunning to count of (every process whose bundle identifier is "com.Growl.GrowlHelperApp") > 0
end tell
         */
        System.out.println("3");
        long count = executeScript(script, 0L);
        System.out.println("4");
        return count > 0;
    }

    private <T> T executeScript(String script, T defaultValue) {
        try {
            return (T) appleScriptEngine.eval(script, appleScriptEngine.getContext());
        } catch (ScriptException e) {
            return defaultValue;
        }
    }

    private void executeScript(String script) {
        try {
            appleScriptEngine.eval(script, appleScriptEngine.getContext());
        } catch (ScriptException e) {
            // log.error("Problem executing script, e);
        }
    }

    private ScriptBuilder script() {
        return new ScriptBuilder();
    }

    private class ScriptBuilder {
        StringBuilder builder = new StringBuilder();

        public ScriptBuilder add(String text) {
            builder.append(text);
            return this;
        }

        public ScriptBuilder quote(String text) {
            builder.append("\"");
            builder.append(text);
            builder.append("\"");
            return this;
        }

        public ScriptBuilder nextRow(String text) {
            builder.append("\n");
            builder.append(text);
            return this;
        }

        public String get() {
            return builder.toString();
        }

        public ScriptBuilder array(String[] array) {
            builder.append("{");
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append("\"");
                builder.append(array[i]);
                builder.append("\"");
            }

            builder.append("}");
            return this;
        }
    }
}

