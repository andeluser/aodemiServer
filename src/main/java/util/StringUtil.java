package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtil {

	//ファイルパスのjsonファイルからマップデータを作成する
	public HashMap<String, Object> readJsonFile(String filePath) throws IOException {

		File tmpFile = null;
		FileInputStream fi = null;
		InputStreamReader is = null;

		//jsonファイルを読み込む
        Path path = Paths.get(filePath);

        //ファイルをコピーする
        String tmpPath = path.toAbsolutePath() + "tmp";
        Path tmp = Paths.get(tmpPath);

		try {

	        //outStream = new FileOutputStream(tmpPath);

			while (true) {
				try {
					Thread.sleep(500);
					Files.copy(path, tmp);
					break;
				} catch (Exception e) {
					AodemiLogger.write("input.json ファイルが操作中のため待機...");
				}
			}

	        tmpFile = new File(tmpPath);
	        fi = new FileInputStream(tmpFile);
	        is = new InputStreamReader(fi,"UTF-8");
	        BufferedReader br = new BufferedReader(is);

	        String text;
	        StringBuffer sb = new StringBuffer();

	        while ((text = br.readLine()) != null) {
	        	sb.append(text);
	        }

	        br.close();

	        ObjectMapper mapper = new ObjectMapper();

	        TypeReference<HashMap<String, Object>> reference = new TypeReference<HashMap<String, Object>>() {};
	        HashMap<String, Object> map = mapper.readValue(sb.toString(), reference);

	        //すべての処理が終わったらtmpを削除する。
	        if(is != null) {
				is.close();
			}

			if (fi != null) {
				fi.close();
			}

			Files.delete(tmp);

			return map;

		} finally {

			if(is != null) {
				is.close();
			}

			if (fi != null) {
				fi.close();
			}

			if (Files.exists(tmp)) {
				Files.delete(tmp);
			}

		}
	}

	//ファイルのマップデータをjsonデータに編集してoutに出力する
	public String serializeJson(HashMap<String, Object> map, String filePath) throws IOException {

		String json = "";
		FileWriter filewriter = null;

		try {
			StringUtil stringUtil = new StringUtil();
			AodemiLogger.writeLogOnly("output:" + stringUtil.getJsonStr(map));

			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

			filewriter = new FileWriter(filePath);
			filewriter.write(json);

			filewriter.close();

			return json;

		} finally {
			if (filewriter != null) {
				filewriter.close();
			}
		}

	}

	//ファイルパスのjsonファイルからマップデータを作成する
	public HashMap<String, Object> getJsonMap(String jsonString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<String, Object>> reference = new TypeReference<HashMap<String, Object>>() {};
        HashMap<String, Object> map = mapper.readValue(jsonString, reference);

		return map;
	}

	//ファイルパスのjsonファイルからマップデータを作成する
	public ArrayList<Object> getJsonArray(String jsonString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        TypeReference<ArrayList<Object>> reference = new TypeReference<ArrayList<Object>>() {};
        ArrayList<Object> list = mapper.readValue(jsonString, reference);

		return list;
	}

	//ファイルのマップデータをjsonデータに編集して返却する
	public String getJsonStr(HashMap<String, Object> map) throws IOException {

		String json = "";

		ObjectMapper mapper = new ObjectMapper();
		json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

		return json;

	}

	//ファイルのArrayデータをjsonデータに編集して返却する
	public String getJsonStr(ArrayList list) throws IOException {

		String json = "";

		ObjectMapper mapper = new ObjectMapper();
		json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);

		return json;

	}

	//文字列が未設定かどうかのチェック
	public boolean checkNull(String str) {

		boolean ret = false;

		if (str == null) {
			ret = true;
		} else if ("".equals(str)) {
			ret = true;
		}

		return ret;
	}

	//文字列をintに変換する
	public int getIntForString(String value) {

		int ret = 0;

		if (value != null && !"".equals(value)) {
			ret = (int)Double.parseDouble(value);
		}

		return ret;
	}

	//設定対象に文字列が含まれる場合は，を付けて返却する
	public String addCardNumberForCamma(String value, int addValue) {

		String ret = "";

		if (value != null && !"".equals(value)) {
			value = value + "," + addValue;
		} else {
			value = "" + addValue;
		}

		ret = value;

		return ret;
	}
}
