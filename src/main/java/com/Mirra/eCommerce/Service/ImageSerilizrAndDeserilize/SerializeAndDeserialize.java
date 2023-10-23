package com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;


public interface SerializeAndDeserialize {

    default byte[] serializeImageList(List<byte[]> imageDataList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(imageDataList);
        }
        return byteArrayOutputStream.toByteArray();
    }

    default List<byte[]> deserializeImageBlob(byte[] imageBlob) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(imageBlob))) {
            return (List<byte[]>) ois.readObject();
        }
    }
}
