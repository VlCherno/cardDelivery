package model;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
        private DataGenerator() {}
        public static OrderFormData generateData() {
            Faker faker = new Faker(new Locale("ru"));
            String[] city = {"Москва", "Санкт-Петербург", "Уфа", "Нальчик",
                    "Махачкала", "Хабаровск", "Барнаул"};
            Random rand = new Random();

            return new OrderFormData(faker.name().name(),
                    faker.phoneNumber().phoneNumber(),
                    city[rand.nextInt(city.length)]
            );
        }
            public static String getOrderDate(int addDays) {
                LocalDate dateOrder = LocalDate.now().plusDays(addDays);
                return dateOrder.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            }
        }