package Controllers;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class InputGeneratorController {
        private static final String latChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static String getLatinStrWithoutSpaces(int count) {
            return RandomStringUtils.random(count, latChars);
        }
        public static Integer getRandomIntegerNumber (){
            return RandomUtils.nextInt();
        }
}
