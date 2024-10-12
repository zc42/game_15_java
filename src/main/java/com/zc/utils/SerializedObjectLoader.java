package com.zc.utils;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.Optional;

import static com.zc.utils.SerializedObjectSaver.getFilePath;
import static com.zc.utils.Utils.prnt;

public class SerializedObjectLoader {

    public static <T> T load(String filePath) {
        Path path = getFilePath(filePath);
        return _load(path);
    }

    @SneakyThrows
    private static <T> T _load(Path filePath) {
//        prnt(MessageFormat.format("loading object from file: {0}", filePath));
        String name = filePath.toAbsolutePath().toString();
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(name);
            in = new ObjectInputStream(file);
            T o = (T) in.readObject();
//            prnt("object created");
            return o;
        } finally {
            if (in != null) {
                in.close();
            }
            if (file != null) {
                file.close();
            }
        }
    }


    public static <T> Optional<T> loadFrom(String filePath) {
        try {
            return Optional.of(SerializedObjectLoader.load(filePath));
        } catch (Throwable throwable) {
            prnt(throwable.getMessage());
            return Optional.empty();
        }
    }
}
