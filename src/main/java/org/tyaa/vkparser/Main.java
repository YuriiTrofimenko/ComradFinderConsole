/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser;

import java.io.*;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tyaa.vkparser.modules.facades.ComradFinder;
import org.tyaa.vkparser.modules.facades.ModelBuilder;

/**
 * Консольный концепт поисковика ссылок на страницы пользователей ВК -
 * потенциальных кандидатов на приглашение в заданную группу
 * 
 * @author Юрий
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    
    //Флаг "полоса прогресса отображается"
    private static volatile boolean cmLoading = false;
    
    //Точка входа
    public static void main(String[] args)
    {
        //Поток ввода из консоли
        BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));
        
        out.println();
        
        out.println("Comrad finder console v1.00");
        
        out.println();
        out.print("Input group id: ");
        String groupIdString = "";
        
        //Получение от пользователя id группы ВК для исследования текстов
        //ее участников
        try {
            groupIdString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        //Начинаем отображение полосы прогресса
        cmLoading = true;
        try {
            loading("Model creating...");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Получаем текстовую информацию со страниц участников групы
        //в формате json, определение частотности
        //слов в различных разделах личной информации
        //и сохранение результатов в файл xml
        ModelBuilder.buildModel(groupIdString);
        
        //Завершаем отображение полосы прогресса
        cmLoading = false;
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        out.print("Input country id: ");
        String countryIdString = "";
        //Получение от пользователя ВК-id  страны,
        //по которой будет происходить поиск кандидатов
        try {
            countryIdString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        out.print("Input city id: ");
        String cityIdString = "";
        //Получение от пользователя ВК-id  города,
        //по которому будет происходить поиск кандидатов
        try {
            cityIdString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        out.print("Input age (from 14 to 80): ");
        String ageString = "";
        //Получение от пользователя age,
        //по которому будет происходить поиск кандидатов
        try {
            ageString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        out.print("Input sex (1 - female or 2 - male): ");
        String sexString = "";
        //Получение от пользователя sex,
        //по которому будет происходить поиск кандидатов
        try {
            sexString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        cmLoading = true;
        try {
            loading("Searching...");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Поиск кандидатов на приглашение с сохранением таблицы
        //результатов в файл Excel по жестко заданному пути 
        //C:/VKParserDoc/ExcellResults/
        ComradFinder.findByModel(countryIdString, cityIdString, ageString, sexString);
        cmLoading = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        out.println("Complete");
        
        try {
            bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Метод отображения полосы прогресса на время длительных операций
    private static synchronized void loading(String msg) throws IOException, InterruptedException {
        
        System.out.println(msg);
        
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.write("\r|".getBytes());
                    while(cmLoading) {
                        System.out.write("-".getBytes());
                        Thread.sleep(1000);
                    }
                    System.out.write("| Done \r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }
}
