package com.example.polls.security;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

@Configuration
public class AesEncryptor implements AttributeConverter<Object, String> {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private final Key key;
    private final IvParameterSpec iv;

    public AesEncryptor() throws Exception {
        key = generateKey();
        iv = generateIV();
    }

    private Key generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom());
            return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }

    private IvParameterSpec generateIV() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] ivBytes = new byte[16];
            secureRandom.nextBytes(ivBytes);
            return new IvParameterSpec(ivBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating IV", e);
        }
    }

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        try {
            if (attribute == null) {
                return null; // Return null if the attribute is null
            }

            // Serialize the object to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(attribute);
            byte[] objectBytes = baos.toByteArray();

            // Add padding to make the length a multiple of 16
            int paddingLength = 16 - (objectBytes.length % 16);
            byte[] paddedBytes = new byte[objectBytes.length + paddingLength];
            System.arraycopy(objectBytes, 0, paddedBytes, 0, objectBytes.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encrypted = cipher.doFinal(paddedBytes);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting attribute", e);
        }
    }

//    @Override
//    public Object convertToEntityAttribute(String dbData) {
//        return null;
//    }


    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null; // Return null if the dbData is null
            }

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(dbData));

            // Deserialize the byte array back to an object
            Object deserializedObject = SerializationUtils.deserialize(decrypted);

            // Handle primitive types
            if (deserializedObject instanceof Integer) {
                return ((Integer) deserializedObject).intValue();
            } else if (deserializedObject instanceof Long) {
                return ((Long) deserializedObject).longValue();
            } else if (deserializedObject instanceof Double) {
                return ((Double) deserializedObject).doubleValue();
            } else if (deserializedObject instanceof Boolean) {
                return ((Boolean) deserializedObject).booleanValue();
            }

            return deserializedObject;
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting attribute", e);
        }



   }
    private byte[] handleIncorrectPadding(Cipher cipher, String dbData) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
            int blockSize = cipher.getBlockSize();
            int paddedLength = encryptedBytes.length + blockSize - (encryptedBytes.length % blockSize);
            byte[] paddedBytes = new byte[paddedLength];
            System.arraycopy(encryptedBytes, 0, paddedBytes, 0, encryptedBytes.length);
            return cipher.doFinal(paddedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error handling incorrect padding", e);
        }
    }
    }
