package lab1;

import java.io.FileReader;
import java.io.FileWriter;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final char[] eng = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] ukr = {'А', 'Б', 'В', 'Г', 'Ґ', 'Д', 'Е', 'Є', 'Ж', 'З', 'И', 'І', 'Ї', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ь', 'Ю', 'Я'};

    public static void main(String[] args) {
        //дата
        Date date = new Date();
        //форматування дати типу День рік_місяць_день година_місяць_секунда
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy_MM_dd hh_mm_ss");
        //змінна для тексту з файлу, який буду потім опрацьовувати
        String text = "";
        //змінна, яка буде зберігати тільки букви і цифри з тексту
        String startText = "";
        //змінна, в якій буде зберігатися мова тексту
        String language = "";
        //змінна для одного символу з тексту
        char ch;
        //змінна для відсоткового значення частоти
        double percent;
        //змінна, яка зберігатиме розмір тексту з цифрами
        int startTextSize;
        //Мапа для сортування частот
        Map<Character, Integer> frequencyMap = new HashMap<>();
        //масив частот для англійської
        int[] frequencyEng = new int[eng.length];
        //масив частот для української
        int[] frequencyUkr = new int[ukr.length];
        //
        List<Map.Entry<Character,Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());

        //Зчитування з файлу
        try (FileReader fileReader = new FileReader("text.txt")){
            //зчитування посимвольно
            //змінна, яка зберігатиме код символу, якщо кінець тексту, то змінна с=-1
            int c;
            //проходжу по всіх символах, поки не буде кінець(змінна с=-1)
            while((c = fileReader.read())> 0){
                //в змінну текст записую кожен символ тексту
                text += String.valueOf((char)c);
            }
            //виводжу в консоль, що зчитано успішно
            System.out.println("File has read correctly");
            //якщо якась помилка, то буде вилітати помилка
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //зберігаю початковий вигляд тексту
        //переводжу текст в верхній регістр
        text = text.toUpperCase();
        //забираю з тексту всі переноси
        text = text.replaceAll("\n", "");
        //забирає всі символи, які означають кінець рядка
        text = text.replaceAll("\r", "");
        //забирає з рядка всі символи, крім укр і англ букв і цифр
        text = text.replaceAll("[^\\dA-Z-А-ЯІЇҐ]", "");
        startText = text;
        //зберігаю розмір тексту разом з буквами і цифрами, щоб потім порахувати частоту
        startTextSize = text.length();
        //забираю всі цифри
        text = text.replaceAll("[^A-Z-А-ЯІЇҐ]", "");
        //
        //якщо текст в файлі після обробки пустий
        if(text.equals("")){
            //виводжу текст
            System.out.println("There is no data that can be processed");
            //закінчую достроково програму
            System.exit(-1);
        }
        //цикл для обчислення частоти
        for (int i = 0; i < text.length(); i++){
            //зберігаю в змінну символ, який знаходиться під індексом "і"
            ch = text.charAt(i);
            //ще один цикл, який буде бігати по масиві з буквами і шукати, яка це буква. Вибрав довжину укр масиву, бо він більший
            for(int j = 0; j < ukr.length; j++){
                //поки розмір англ масиву букв більший за індекс "j", перевіряю для англ масиву, бо якщо буде більший індекс, то він вийде за межі масиву
                if(eng.length > j) {
                    //якщо символ рівний символу з англ масиву під індексом "j", то в англ масиві частот для цього індекса додаю 1
                    if (ch == eng[j]) {
                        frequencyEng[j]++;
                        //оскільки найшли англ символ, то мова англійська
                        language = "eng";
                    }
                }
                //якщо символ рівний символу з укр масиву під індексом "j", то в укр масиві частот для цього індекса додаю 1
                if(ch == ukr[j]){
                    frequencyUkr[j]++;
                    //оскільки найшли укр символ, то мова укр
                    language = "ukr";
                }
            }
        }

        //сортування частот
        if (language.equals("eng")) {
            //заповнюю мапу
            for (int i = 0; i < frequencyEng.length; i++){
                frequencyMap.put(eng[i], frequencyEng[i]);
            }
        } else {
            for (int i = 0; i < frequencyUkr.length; i++){
                frequencyMap.put(ukr[i], frequencyUkr[i]);
            }
        }
        frequencyMap = Main.sortByValue(frequencyMap);

        //записування частоти в файл з назвою   "res_fr ДАТА.txt
        try (FileWriter fileWriter = new FileWriter("res_fr "+ formatForDateNow.format(date) +".txt")) {
            fileWriter.write("String:\n" + startText + "\n");
            fileWriter.write("______________________________\n");
            fileWriter.write(language.toUpperCase() + "\n");
            fileWriter.write("Char\tFreq\n");
            //цикл, в якому обчислюю вдсоткову частоту
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                //формула обчислювання, якщо буде питати, нащо (double) startTextSize, то скажеш, що під час ділення цілих результат заокруглюється, а так нє
                percent = (entry.getValue() / (double) startTextSize) * 100;
                fileWriter.write(entry.getKey() + "\t" + percent + "%" + "\n");
            }
            System.out.println("Done(frequency)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter("res_sorted "+ formatForDateNow.format(date) +".txt")) {
            fileWriter.write("String:\n" + startText + "\n");
            fileWriter.write("______________________________\n");
            fileWriter.write("String sorted by alphabet:\n");
            fileWriter.write(sortByAlphabet(startText.toUpperCase(), language)+"\n");
            fileWriter.write("String sorted by frequency:\n");
            if (language.equals("eng")) {
                fileWriter.write(sortByFrequency(frequencyEng, language) + "\n");
            } else {
                fileWriter.write(sortByFrequency(frequencyUkr, language) + "\n");
            }

            System.out.println("Done(sorting)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //метод сортування за алфавітом, передаю стрічку notSorted і мову language
    public static String sortByAlphabet(String notSorted, String language){
        //з стрічки роблю масив символів
        char[] chars = notSorted.toCharArray();
        //якщо мова українська
        if (language.equals("ukr")) {

            String res="";
            List<String> characterList = new ArrayList<>();
            for (char c : notSorted.toCharArray()) {
                characterList.add(String.valueOf(c));
            }
            Collator coll = Collator.getInstance(new Locale("uk", "UA"));
            List<String> sorted = characterList.stream().sorted(coll).collect(Collectors.toList());
            for (int i = 0; i < sorted.size(); i++){
                res+=sorted.get(i);
            }
            return res;
        } else {
            //сортування англійської
            Arrays.sort(chars);
            return new String(chars);
        }
    }

    //метод сортування за частотою передаю масив частот та мову
    public static String sortByFrequency(int[] frequency, String language){
        String sorted = "";
        //створюю "мап", ключ якої буде символ, а значення-частота
        Map<Character, Integer> frequencyMap = new HashMap<>();
        //якщо мова англійська
        if (language.equals("eng")) {
            //заповнюю мапу
            for (int i = 0; i < frequency.length; i++){
                frequencyMap.put(eng[i], frequency[i]);
            }

            List<Map.Entry<Character,Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());
            sortedList.sort((o1, o2) -> o2.getValue() - o1.getValue());

            for (Map.Entry<Character,Integer>  e : sortedList)
            {
                for(int j = 0; j <  e.getValue(); j++) {
                    sorted += e.getKey().toString();
                }
            }
        } else {
            for (int i = 0; i < frequency.length; i++){
                frequencyMap.put(ukr[i], frequency[i]);
            }
            List<Map.Entry<Character,Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());
            sortedList.sort((o1, o2) -> o2.getValue() - o1.getValue());

            for (Map.Entry<Character,Integer>  e : sortedList)
            {
                for(int j = 0; j <  e.getValue(); j++) {
                    sorted += e.getKey().toString();
                }
            }
        }
        return sorted;
    }

    private static Map<Character, Integer> sortByValue(Map<Character, Integer> unsortMap) {
        // 1. Convert Map to List of Map
        List<Map.Entry<Character, Integer>> list =
                new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            public int compare(Map.Entry<Character, Integer> o1,
                               Map.Entry<Character, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<Character, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


}
