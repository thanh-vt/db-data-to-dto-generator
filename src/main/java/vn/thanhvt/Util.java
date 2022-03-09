package vn.thanhvt;

import javafx.scene.control.Alert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Util {

    public static String snakeToCamelCase(String str) {
        // Capitalize first letter of string
        str = str.substring(0, 1).toUpperCase()
                + str.substring(1);

        // Convert to StringBuilder
        StringBuilder builder
                = new StringBuilder(str);

        // Traverse the string character by
        // character and remove underscore
        // and capitalize next letter
        for (int i = 0; i < builder.length(); i++) {

            // Check char is underscore
            if (builder.charAt(i) == '_') {

                builder.deleteCharAt(i);
                builder.replace(
                        i, i + 1,
                        String.valueOf(
                                Character.toUpperCase(
                                        builder.charAt(i))));
            }
        }

        // Return in String type
        return builder.toString();
    }

    public static String snakeToPascalCase(String str) {
        String[] parts = str.split("_");

        StringBuilder pascalString = new StringBuilder();

        for (String string : parts) {

            String temp = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
            pascalString.append(temp);

        }
        return pascalString.toString();
    }

    public static Map<String, Method> generateMethodMap(Class<?> clazz, Map<String, Method> methodMap) {
        for (Method method: clazz.getDeclaredMethods()) {
            methodMap.put(method.getName(), method);
        }
        if (clazz.getSuperclass() != null) {
            generateMethodMap(clazz.getSuperclass(), methodMap);
        }
        return methodMap;
    }

    public static void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");

        // Header Text: null
        alert.setHeaderText("Error: " + e.getMessage());
        e.printStackTrace();
        StringBuilder sb = new StringBuilder();
        int max = 0;
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append(stackTraceElement.getFileName())
                    .append(" (line ").append(stackTraceElement.getLineNumber())
                    .append("): ").append(stackTraceElement.getClassName())
                    .append(".").append(stackTraceElement.getMethodName())
                    .append("\n");
            if (max >= 10) {
                sb.append("...");
                break;
            }
            max++;
        }
        alert.setContentText(sb.toString());
        alert.setWidth(600);
        alert.setHeight(400);
        alert.showAndWait();
    }

}
