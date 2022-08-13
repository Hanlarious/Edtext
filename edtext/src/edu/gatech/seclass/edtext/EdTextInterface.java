package edu.gatech.seclass.edtext;

/**
 * Interface created for use in Georgia Tech CS6300.
 * <p>
 * IMPORTANT: This interface should NOT be altered in any way.
 */
public interface EdTextInterface {

    /**
     * Reset the EdText object to its initial state, for reuse.
     */
    void reset();

    /**
     * Sets the path of the input file. This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param filepath The file path to be set.
     */
    void setFilepath(String filepath);

    /**
     * Set to replace the first instance of string old in each line
     * with string new.  The search is case-sensitive.
     * This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param oldString The string to be replaced
     * @param newString The new string replacing oldString
     */
    void setReplaceString(String oldString, String newString);

    /**
     * Set to replaces all occurrences of string old in
     * each line with string new. Used with the -r flag ONLY;
     * This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param globalReplace Flag to toggle functionality
     */
    void setGlobalReplace(boolean globalReplace);

    /**
     * Set to convert all ASCII printable characters (ASCII codes
     * 32-126, inclusive) to the corresponding ASCII code (for
     * example, a = 97) followed by a single space.
     * This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param asciiConvert Flag to toggle functionality
     */
    void setAsciiConvert(boolean asciiConvert);

    /**
     * Sets the prefix. This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param prefix The suffix to be set.
     */
    void setPrefix(String prefix);

    /**
     * Set to duplicate each line in the file n times,
     * where n is an integer in the inclusive range of 1 to 10.
     * The duplicate lines should be sequential to each other.
     * This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param duplicateLine   Flag to toggle functionality
     * @param duplicateFactor Number of times to duplicate the line
     */
    void setDuplicateLines(final boolean duplicateLine, int duplicateFactor);

    /**
     * Set to add line numbers to each line, with the amount of
     * padding based upon the width parameter, starting from 1.
     * This method has to be called before invoking the
     * {@link #edtext()} methods.
     *
     * @param addLineNumber Flag to toggle functionality.
     * @param width         The amount of padding to be used.
     */
    void setAddLineNumber(final boolean addLineNumber, int width);

    /**
     * Set to edit file in place.
     * When set, the program overwrites the input file
     * with transformed text instead of writing to stdout.
     * This method has to be called
     * before invoking the {@link #edtext()} methods.
     *
     * @param inplaceEdit Flag to toggle functionality
     */
    void setInplaceEdit(boolean inplaceEdit);

    /**
     * Outputs a System.lineSeparator() delimited string that contains
     * selected parts of the lines in the file specified using {@link #setFilepath}
     * and according to the current configuration, which is set
     * through calls to the other methods in the interface.
     * <p>
     * It throws a {@link EdTextException} if an error condition
     * occurs (e.g., when the specified file does not exist).
     *
     * @throws EdTextException thrown if an error condition occurs
     */
    void edtext() throws EdTextException;
}
