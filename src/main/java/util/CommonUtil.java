package util;

import java.io.IOException;
import java.util.HashMap;

public class CommonUtil {

	//入力したゲージ、ストック、増減値から更新後のゲージ、ストックを計算する
	public HashMap<String, Object> addSpecial(int befGage, int befStock, int addGage, int addStock) throws IOException {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		int gage = befGage + addGage;
		int stock = befStock + addStock;

		if (addGage > 0 || addStock > 0) {
			//加算の場合
			//ゲージが２０を超えていた場合
			while (gage >= 20) {
				gage = gage - 20;
				stock = stock + 1;
			}

			if (stock >= 5) {
				gage = 0;
				stock = 5;
			}

		} else if (addGage < 0 || addStock < 0) {
			//減算の場合
			while (gage <= 0) {
				gage = gage + 20;
				stock = stock - 1;
			}

			if (gage == 20) {
				gage = 0;
				stock  = stock + 1;
			}

			if (stock < 0) {
				gage = 0;
				stock = 0;
			}

		} else if (addGage == 0 && addStock == 0) {
			//どちらも０の場合は何もしない
			gage = befGage;
			stock = befStock;
		}

		ret.put("gage", gage);
		ret.put("stock", stock);

		return ret;
	}

}
