package com.xceptance.multibrowser.runner;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.xceptance.multibrowser.AbstractAnnotatedScriptTestCase;

/**
 * JUnit runner used to run testcases that inherit from {@link AbstractAnnotatedScriptTestCase}. This class reads the
 * annotation based configuration of {@link TestTarget} and executes the testcase multiple-times with different
 * configurations.
 * 
 * @author m.kaufmann
 * @see {@link AbstractAnnotatedScriptTestCase}, {@link TestTarget}
 */
public class AnnotationRunner extends BlockJUnit4ClassRunner
{

    public AnnotationRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    // /**
    // * The list of directories to be searched for data set files.
    // */
    // private static final List<File> dataSetFileDirs = new ArrayList<File>();
    //
    // /**
    // * An empty data set.
    // */
    // private static final Map<String, String> EMPTY_DATA_SET = Collections.emptyMap();
    //
    // /**
    // * The current directory.
    // */
    // protected static final File CURRENT_DIR = new File(".");
    //
    // /**
    // * The data sets directory as specified in the XLT configuration. Maybe <code>null</code> if not configured.
    // */
    // protected static final File DATA_SETS_DIR;
    //
    // private static final String SYSTEM_PROPERTY_BROWSERDEFINITION = "browserdefinition";
    //
    // private final ProxyConfigurationDto proxyConfig;
    //
    // static
    // {
    // final String dataSetFileDirectoryName =
    // XltProperties.getInstance().getProperty("com.xceptance.xlt.data.dataSets.dir", "");
    // if (dataSetFileDirectoryName.length() > 0)
    // {
    // final File dir = new File(dataSetFileDirectoryName);
    //
    // DATA_SETS_DIR = dir.isDirectory() ? dir : null;
    // }
    // else
    // {
    // DATA_SETS_DIR = null;
    // }
    //
    // // 1. the current directory
    // dataSetFileDirs.add(CURRENT_DIR);
    //
    // // 2. the data sets directory if available
    // if (DATA_SETS_DIR != null)
    // {
    // dataSetFileDirs.add(DATA_SETS_DIR);
    // }
    //
    // // 3. the scripts directory
    // dataSetFileDirs.add(XlteniumScriptInterpreter.SCRIPTS_DIRECTORY);
    // }
    //
    // /**
    // * The JUnit children of this runner.
    // */
    // private final List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>();
    //
    // /**
    // * Sets the test instance up.
    // *
    // * @param method
    // * the method
    // * @param test
    // * the test instance
    // */
    // protected void setUpTest(final FrameworkMethod method, final Object test)
    // {
    // if (test instanceof AbstractScriptTestCase)
    // {
    // // set the test data set at the test instance
    // final AnnotatedFrameworkMethod frameworkMethod = (AnnotatedFrameworkMethod) method;
    //
    // // get the browser configuration for this testcase
    // final BrowserConfigurationDto config = frameworkMethod.getBrowserConfiguration();
    //
    // // instantiate webdriver according to browser configuration
    // WebDriver driver = null;
    // try
    // {
    // driver = AnnotationRunnerHelper.createWebdriver(config, proxyConfig);
    // }
    // catch (final MalformedURLException e)
    // {
    // throw new RuntimeException("An error occured during URL creation. See nested exception.", e);
    // }
    // if (driver != null)
    // {
    // // set browser window size
    // AnnotationRunnerHelper.setBrowserWindowSize(config, driver);
    // ((AbstractScriptTestCase) test).setWebDriver(driver);
    // ((AbstractScriptTestCase) test).setTestDataSet(frameworkMethod.getDataSet());
    // }
    // else
    // {
    // throw new RuntimeException("Could not create driver for browsertag: " + config.getConfigTag() +
    // ". Please check your browserconfigurations.");
    // }
    // }
    // }
    //
    // public AnnotationRunner(final Class<?> testCaseClass) throws Throwable
    // {
    // this(testCaseClass, ScriptingUtils.getScriptName(testCaseClass),
    // ScriptingUtils.getScriptBaseName(ScriptingUtils.getScriptName(testCaseClass)), dataSetFileDirs);
    // }
    //
    // public AnnotationRunner(final Class<?> testCaseClass, final String testCaseName, final String defaultTestMethodName,
    // final List<File> dataSetFileDirs)
    // throws Throwable
    // {
    // super(testCaseClass);
    //
    // // get the short (package-less) test case name
    // final String shortTestCaseName = StringUtils.contains(testCaseName, '.') ?
    // StringUtils.substringAfterLast(testCaseName, ".")
    // : testCaseName;
    // // get the data sets
    // final List<Map<String, String>> dataSets = getDataSets(testCaseClass, testCaseName, shortTestCaseName,
    // dataSetFileDirs);
    //
    // final XltProperties xltProperties = XltProperties.getInstance();
    //
    // // parse proxy settings
    // proxyConfig = new PropertiesToProxyConfigurationMapper().toDto(xltProperties);
    //
    // // parse browser properties
    // final Map<String, BrowserConfigurationDto> parsedBrowserProperties =
    // AnnotationRunnerHelper.parseBrowserProperties(xltProperties);
    //
    // final String ieDriverPath = xltProperties.getProperty(XltPropertyKey.WEBDRIVER_PATH_IE, null);
    // final String chromeDriverPath = xltProperties.getProperty(XltPropertyKey.WEBDRIVER_PATH_CHROME, null);
    // final String geckoDriverPath = xltProperties.getProperty(XltPropertyKey.WEBDRIVER_PATH_FIREFOX, null);
    //
    // // shall we run old school firefox?
    // final boolean firefoxLegacy = xltProperties.getProperty(XltPropertyKey.WEBDRIVER_FIREFOX_LEGACY, false);
    // System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, Boolean.toString(!firefoxLegacy));
    //
    // if (!StringUtils.isEmpty(ieDriverPath))
    // {
    // System.setProperty("webdriver.ie.driver", ieDriverPath);
    // }
    // if (!StringUtils.isEmpty(chromeDriverPath))
    // {
    // System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    // }
    // if (!StringUtils.isEmpty(geckoDriverPath))
    // {
    // System.setProperty("webdriver.gecko.driver", geckoDriverPath);
    // }
    // boolean foundTargetsAnnotation = false;
    //
    // // get test specific browser definitions (aka browser tag see browser.properties)
    // // could be one value or comma separated list of values
    // String browserDefinitionsProperty = XltProperties.getInstance().getProperty(SYSTEM_PROPERTY_BROWSERDEFINITION, "");
    // if (browserDefinitionsProperty != null)
    // browserDefinitionsProperty = browserDefinitionsProperty.replaceAll("\\s", "");
    //
    // List<String> browserDefinitions = null;
    //
    // // parse test specific browser definitions
    // if (!StringUtils.isEmpty(browserDefinitionsProperty))
    // {
    // browserDefinitions = Arrays.asList(browserDefinitionsProperty.split(","));
    // }
    //
    // // Get annotations of test class.
    // final Annotation[] annotations = testCaseClass.getAnnotations();
    // for (final Annotation annotation : annotations)
    // {
    // // only check TestTargets annotation with a list of nested TestTarget annotations
    // if (annotation instanceof TestTargets)
    // {
    // foundTargetsAnnotation = true;
    //
    // final String[] targets = ((TestTargets) annotation).value();
    //
    // for (final String target : targets)
    // {
    // // check if the annotated target is in the list of targets specified via system property
    // if (browserDefinitions != null && !browserDefinitions.contains(target))
    // {
    // continue;
    // }
    //
    // final BrowserConfigurationDto foundBrowserConfiguration = parsedBrowserProperties.get(target);
    // if (foundBrowserConfiguration == null)
    // {
    // throw new IllegalArgumentException("Can not find browser configuration with tag: " + target);
    // }
    //
    // for (final FrameworkMethod frameworkMethod : getTestClass().getAnnotatedMethods(Test.class))
    // {
    // // get the test method to run
    // final Method testMethod = frameworkMethod.getMethod();
    //
    // // check whether to override the test method name
    // final String testMethodName = (defaultTestMethodName == null) ? testMethod.getName() : defaultTestMethodName;
    //
    // // create the JUnit children
    // if (dataSets == null || dataSets.isEmpty())
    // {
    // methods.add(new AnnotatedFrameworkMethod(frameworkMethod.getMethod(), testMethodName, foundBrowserConfiguration,
    // -1, EMPTY_DATA_SET));
    // }
    // else
    // {
    // // run the method once for each data set
    // int i = 0;
    // for (final Map<String, String> dataSet : dataSets)
    // {
    // methods.add(new AnnotatedFrameworkMethod(frameworkMethod.getMethod(), testMethodName,
    // foundBrowserConfiguration, i++, dataSet));
    // }
    // }
    // }
    // }
    // }
    // }
    //
    // if (!foundTargetsAnnotation)
    // throw new IllegalClassException("The class (" + testCaseClass.getSimpleName() +
    // ") does not have a required TestTargets annotation.");
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // protected List<FrameworkMethod> getChildren()
    // {
    // return methods;
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // public Description getDescription()
    // {
    // final Description description = Description.createSuiteDescription(getTestClass().getJavaClass());
    //
    // for (final FrameworkMethod frameworkMethod : getChildren())
    // {
    // description.addChild(Description.createTestDescription(getTestClass().getJavaClass(), frameworkMethod.getName()));
    // }
    //
    // return description;
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // protected Statement methodInvoker(final FrameworkMethod method, final Object test)
    // {
    // // prepare the test instance before executing it
    // setUpTest(method, test);
    //
    // // the real job is done here
    // return super.methodInvoker(method, test);
    // }
    //
    // /**
    // * Returns the test data sets associated with the given test case class.
    // *
    // * @param testClass
    // * the test case class
    // * @param fullTestCaseName
    // * the full test case name
    // * @param shortTestCaseName
    // * the short test case name
    // * @param dataSetFileDirs
    // * the list of directories to search for data set files
    // * @return the data sets, or <code>null</code> if there are no associated test data sets
    // * @throws DataSetProviderException
    // * if the responsible data set provider cannot be created
    // * @throws FileNotFoundException
    // * if an explicitly configured data set file cannot be found
    // * @throws IOException
    // */
    // private List<Map<String, String>> getDataSets(final Class<?> testClass, final String fullTestCaseName, final String
    // shortTestCaseName,
    // final List<File> dataSetFileDirs)
    // throws DataSetProviderException, FileNotFoundException, IOException
    // {
    // // check whether we are in load test mode
    // if (Session.getCurrent().isLoadTest())
    // {
    // // data set providers are not supported in load test mode
    // return null;
    // }
    //
    // // check whether data-driven tests are enabled
    // final boolean enabled = XltProperties.getInstance().getProperty("com.xceptance.xlt.data.dataDrivenTests.enabled",
    // true);
    // if (!enabled)
    // {
    // return null;
    // }
    //
    // // check whether a specific file has been configured
    // final String specificFileNameKey1 = testClass.getName() + ".dataSetsFile";
    // String specificFileName = XltProperties.getInstance().getProperty(specificFileNameKey1, "");
    // if (specificFileName.length() == 0)
    // {
    // final String specificFileNameKey2 = fullTestCaseName + ".dataSetsFile";
    // specificFileName = XltProperties.getInstance().getProperty(specificFileNameKey2, "");
    // }
    //
    // if (specificFileName.length() != 0)
    // {
    // // there is a specific file
    // File batchDataFile = new File(specificFileName);
    // if (batchDataFile.isAbsolute())
    // {
    // // absolute -> try it as is
    // return readDataSets(batchDataFile);
    // }
    // else
    // {
    // // relative -> search for it in the usual directories
    // for (final File directory : dataSetFileDirs)
    // {
    // batchDataFile = new File(directory, specificFileName);
    // if (batchDataFile.isFile())
    // {
    // return readDataSets(batchDataFile);
    // }
    // }
    //
    // throw new FileNotFoundException("Specific test data set file name configured, but file could not be found: " +
    // specificFileName);
    // }
    // }
    // else
    // {
    // // no specific file -> try the usual suspects
    // final Set<String> fileNames = new LinkedHashSet<String>();
    //
    // final String dottedName = fullTestCaseName;
    // final String slashedName = dottedName.replace('.', '/');
    //
    // final DataSetProviderFactory dataSetProviderFactory = DataSetProviderFactory.getInstance();
    // for (final String fileExtension : dataSetProviderFactory.getRegisteredFileExtensions())
    // {
    // final String suffix = "_datasets." + fileExtension;
    //
    // fileNames.add(slashedName + suffix);
    // fileNames.add(dottedName + suffix);
    // }
    //
    // // look for such a file in the usual directories
    // return getDataSets(dataSetFileDirs, fileNames, testClass);
    // }
    // }
    //
    // /**
    // * Looks for a data set file and, if found, returns its the data sets. Tries all the specified file names in all the
    // * passed directories and finally in the class path.
    // *
    // * @param dataSetFileDirs
    // * the directories to search
    // * @param fileNames
    // * the file names to try
    // * @param testClass
    // * the test case class as the class path context
    // * @return the data sets, or <code>null</code> if no data sets file was found
    // * @throws IOException
    // * if an I/O error occurred
    // * @throws DataSetProviderException
    // * if there is no responsible data set provider
    // */
    // private List<Map<String, String>> getDataSets(final List<File> dataSetFileDirs, final Set<String> fileNames, final
    // Class<?> testClass)
    // throws IOException
    // {
    // // look for a data set file in the passed directories
    // for (final File directory : dataSetFileDirs)
    // {
    // for (final String fileName : fileNames)
    // {
    // final File batchDataFile = new File(directory, fileName);
    // if (batchDataFile.isFile())
    // {
    // return readDataSets(batchDataFile);
    // }
    // }
    // }
    //
    // // look for a data set file in the class path
    // for (final String fileName : fileNames)
    // {
    // final InputStream input = testClass.getResourceAsStream("/" + fileName);
    // if (input != null)
    // {
    // OutputStream output = null;
    // File batchDataFile = null;
    //
    // try
    // {
    // // copy the stream to a temporary file
    // final String extension = "." + FilenameUtils.getExtension(fileName);
    // batchDataFile = File.createTempFile("dataSets_", extension);
    // output = new FileOutputStream(batchDataFile);
    //
    // IOUtils.copy(input, output);
    // output.flush();
    //
    // // read the data sets from the temporary file
    // return readDataSets(batchDataFile);
    // }
    // finally
    // {
    // // clean up
    // IOUtils.closeQuietly(input);
    // IOUtils.closeQuietly(output);
    // FileUtils.deleteQuietly(batchDataFile);
    // }
    // }
    // }
    //
    // return null;
    // }
    //
    // /**
    // * Returns the test data sets contained in the given test data file. The data set provider used to read the file is
    // * determined from the data file's extension.
    // *
    // * @param dataSetsFile
    // * the test data set file
    // * @return the data sets
    // * @throws DataSetProviderException
    // * if there is no responsible data set provider
    // */
    // private List<Map<String, String>> readDataSets(final File dataSetsFile) throws DataSetProviderException
    // {
    // XltLogger.runTimeLogger.debug("Test data set file used: " + dataSetsFile.getAbsolutePath());
    //
    // final DataSetProviderFactory dataSetProviderFactory = DataSetProviderFactory.getInstance();
    // final String fileExtension = FilenameUtils.getExtension(dataSetsFile.getName());
    // final DataSetProvider dataSetProvider = dataSetProviderFactory.createDataSetProvider(fileExtension);
    //
    // return dataSetProvider.getAllDataSets(dataSetsFile);
    // }
}
