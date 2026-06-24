package BaiTap;
/*
Bài 6: Viết chương trình nhập vào một đoạn văn bản, đếm số lần xuất hiện của mỗi từ
Gợi ý:
• Dùng Map<String, Integer>
• Tách chuỗi bằng split("\\s+")
*/

import static function.KeyBoard.readString;

import java.util.HashMap;
import java.util.Map;

public class B6 {
    public static void main(String[] args) {
        String text = readString("Nhap doan van: ");

        String[] words = text.split("\\s+");

        Map<String, Integer> map = new HashMap<>();

        for (String word : words) {
            if(map.containsKey(word)){
                map.put(word, map.get(word)+1);
            }else{
                map.put(word, 1);
            }
        }

        System.out.println("So lan xuat hien cua moi tu");
        for (String key : map.keySet()) {
            System.out.println(key + ": " + map.get(key));
        }
    }
}
