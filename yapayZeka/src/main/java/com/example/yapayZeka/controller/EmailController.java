package com.example.yapayZeka.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

@RestController
public class EmailController {
    // Weka modellerini yükleme
    private NaiveBayes model1;
    private NaiveBayes model2;
    private Instances data1;
    private Instances data2;

    public EmailController() {
        try {
            // Model1 yükleme
            model1 = (NaiveBayes) SerializationHelper.read("https://github.com/bugraklcc/yapayZeka-5-/blob/main/yapayZeka/model/model1");
            data1 = new Instances(new java.io.FileReader("https://github.com/bugraklcc/yapayZeka-5-/blob/main/yapayZeka/arff/norm_spam1.arff"));
            data1.setClassIndex(data1.numAttributes() - 1);

            // Model2 yükleme
            model2 = (NaiveBayes) SerializationHelper.read("https://github.com/bugraklcc/yapayZeka-5-/blob/main/yapayZeka/model/norm_spam_model.model");
            data2 = new Instances(new java.io.FileReader("https://github.com/bugraklcc/yapayZeka-5-/blob/main/yapayZeka/arff/norm_spam.arff"));
            data2.setClassIndex(data2.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param input
     * @return
     */
  
    @PostMapping("/")
    public String classifyEmail(@RequestParam String input) {
        // Gelen maili normal/spam olarak sınıflandırma
        double[] instanceValues = new double[data1.numAttributes()];
        String[] inputValues = input.split(",");
        for (int i = 0; i < inputValues.length; i++) {
            instanceValues[i] = Double.parseDouble(inputValues[i]);
        }
        double result;
        try {
            DenseInstance instance = new DenseInstance(1.0, instanceValues);
            for (int i = 0; i < instanceValues.length; i++) {
                instance.setValue(i, instanceValues[i]);
            }
            result = model1.classifyInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        // Eğer gelen mail normal ise, sınıflandırmaya gerek yok.
        if (result == 0) {
            return "normal";
        }

        // Eğer gelen mail spam ise, spam filtresine gönder.
        double[] instanceValues2 = new double[data2.numAttributes()];
        for (int i = 0; i < inputValues.length; i++) {
            instanceValues2[i] = Double.parseDouble(inputValues[i]);
        }
        double result2;
        try {
            result2 = model2.classifyInstance(new DenseInstance(1.0, instanceValues2));
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        // Spam filtresinden geçen mailler "spam" olarak işaretlenir.
        if (result2 == 0) {
            return "spam";
        }

        // Diğer durumlarda hata döndürülür.
        return "error";
    }
}