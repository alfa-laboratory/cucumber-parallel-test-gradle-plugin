package ru.alfalab.gradle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureReader {

    private static final String TAG_PATTERN = "@\\w+";

    /**
     * Исключаем из списка feature файлов те которые не содержат запускаемые теги
     */
    static Set<File> filterByTags(Set<File> featureSet, String tags) throws IOException {
        if (tags == null) {
            return featureSet;
        }
        List<String> tagList = new ArrayList<>();
        Set<File> newFeatureSet = new HashSet<>();
        String s;

        Matcher p = Pattern.compile(TAG_PATTERN).matcher(tags);
        while (p.find())
            tagList.add(p.group());

        for (String tag : tagList) {
            for (File f : featureSet) {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                while ((s = br.readLine()) != null) {
                    if (s.contains(tag)) {
                        newFeatureSet.add(f);
                        break;
                    }
                }
                fr.close();
            }
        }
        return newFeatureSet;
    }

}
