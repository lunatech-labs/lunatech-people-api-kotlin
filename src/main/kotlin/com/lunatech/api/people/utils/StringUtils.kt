package com.lunatech.api.people.utils

class StringUtils {

    companion object {

        val ALL_NON_ALPHABETS = "[^A-Za-z]".toRegex()
        val EMPTY_CHARACTER = ""
        val SPACE_CHARACTER = " "

        val DELIMETER_OR = ","
        val DELIMETER_AND = ";"

        /**
         * Processes a piece of text by removing
         * all non-alphabet characters, trimming
         * the beginning and end of the text to remove
         * any spaces, and converting each character
         * to upper case. Helper function in equating
         * two pieces of noisy text.
         *
         * Sample Input -> Output:
         * " Nicolas leRoux09" -> "NICOLASLEROUX"
         */
        fun tidyText(textToTidy: String): String {
            return textToTidy
                    .replace(ALL_NON_ALPHABETS, EMPTY_CHARACTER)
                    .trim()
                    .toUpperCase()
        }

        /**
         * Parses a given string to check if it
         * specifies the OR or AND operation.
         * The String is checked for the existence
         * of either delimeter mapped to the operations.
         * An array indicating which delimeters were found
         * is returned.
         *
         * Sample Input -> Output:
         * "dev,dev-manager" -> "[0 1]"
         * "dev;dev-manager" -> "[1 0]"
         */
        fun parseAndOrString(toParse: String): IntArray {
            var delimeters = listOf(DELIMETER_AND, DELIMETER_OR)
            var found = IntArray(delimeters.size, { Constants.NOT_SATISFIED })

            for (i in (0..delimeters.size - 1)) {
                val d = delimeters[i]

                if(toParse.contains(d)) {
                    found[i] = Constants.SATISFIED
                }
            }

            return found
        }

        /**
         * Processes a piece of text by removing
         * all spaces.
         *
         * Sample Input -> Output:
         * " Nicolas leRoux09" -> "NICOLASLEROUX"
         */
        fun removeSpaces(text: String): String {
            return text.replace(SPACE_CHARACTER, EMPTY_CHARACTER)
        }

    }

}