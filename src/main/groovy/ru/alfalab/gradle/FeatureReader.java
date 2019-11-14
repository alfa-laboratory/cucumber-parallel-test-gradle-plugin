package ru.alfalab.gradle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureReader {

    private static final String TAG_PATTERN = "@\\w+[-\\S]\\w+";

    /**
     * Исключаем из списка feature файлов те которые не содержат запускаемые теги
     */
    static Set<File> filterByTags(Set<File> featureSet, String tags) throws IOException {

        if (tags == null) {
            return featureSet;
        }

        Set<File> newFeatureSet = new HashSet<>();

        for (String tag : parseTag(tags)) {
            for (File f : featureSet) {
                for (String s : readFromFile(f))
                    if (s.contains(tag)) {
                        newFeatureSet.add(f);
                        break;
                    }
            }
        }
        return newFeatureSet;
    }

    static Set<String> parseTag(String tags) {
        Set<String> tagList = new HashSet<>();
        Matcher p = Pattern.compile(TAG_PATTERN).matcher(tags);
        while (p.find())
            tagList.add(p.group());
        return tagList;
    }

    static Set<String> readFromFile(File f) {
        Set<String> fileLineSet = new HashSet<>();
        String s;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while ((s = br.readLine()) != null)
                fileLineSet.add(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileLineSet;
    }

}
