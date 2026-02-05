package se.yrgo.schedule.format;

/**
 * A factory to get a formatter (XML or JSON)
 */
public class FormatterFactory {

  private static Formatter XML_FORMATTER = new XMLFormatter();
  //private static Formatter HTML_FORMATTER = new HtmlFormatter();
  private static Formatter JSON_FORMATTER = new JsonFormatter();

  /**
   * Returns a formatter for the given contentType
   * @param The content type you want to format to (XML or JSON)
   * @return A Formatter of the correct type, depending on the provided
   * contentType.
   * @throws IllegalArgumentException if format is other than xml or json.
   */
  public static Formatter getFormatter(String contentType) {
    if (contentType.equalsIgnoreCase("xml")) {
      return XML_FORMATTER;
    } else if (contentType.equalsIgnoreCase("json")) {
      return JSON_FORMATTER;
    } else {
        throw new IllegalArgumentException("Format not supported");
    }
  }
}
