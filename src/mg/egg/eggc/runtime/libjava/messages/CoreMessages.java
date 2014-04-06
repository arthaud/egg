package mg.egg.eggc.runtime.libjava.messages;

import java.util.ResourceBundle;

public class CoreMessages extends NLS {
	private static final long serialVersionUID = 1L;

	private static final String BUNDLE_NAME = "mg.egg.eggc.runtime.libjava.messages.CoreMessages";

	private CoreMessages() {
		// Do not instantiate
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, CoreMessages.class);
	}

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	public static String EGG_tds_read_error;

	public static String EGG_tds_deserialisation_error;

	public static String EGG_tds_serialisation_error;

	public static String EGG_severity_error;

	public static String EGG_severity_warning;

	public static String EGG_category_internal;

	public static String EGG_category_syntax;

	public static String EGG_category_semantics;

	public static String EGG_source_read_error;

	public static String EGG_conflict;

	public static String EGG_file_creation_error;

	public static String EGG_source_contents_error;

	public static String EGG_scanner_error;

	public static String EGG_left_recursion;

	public static String EGG_nt_undefined_error;

	public static String EGG_not_a_folder_error;

	public static String EGG_config_file_error;

	public static String EGG_runtime_error;
}
