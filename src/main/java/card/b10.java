package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleFieldDAO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//祝福された兵士
public class b10 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	//鷹の目
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
				return null;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) {
		// TODO 自動生成されたメソッド・スタブ
			return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		ArrayList<Object> retList = new ArrayList<Object>();

		//自分のユニットのDEFを０にする
		int def = fieldDto.getPermanent_def() + fieldDto.getTurn_def() + fieldDto.getCur_def();

		//DEFゼロなら処理終了
		if (def == 0) {
			return ret;
		}

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);

		if (fieldDto.getPermanent_def() != 0) {
			detailMap.put("upDEF", 0);
			fieldDto.setPermanent_def(0);
		}

		if (fieldDto.getTurn_def() != 0) {
			detailMap.put("tupDEF", 0);
			fieldDto.setTurn_def(0);
		}

		//DEFの２倍をATKに
		fieldDto.setTurn_atk(fieldDto.getTurn_atk() + (def* 2));
		detailMap.put("tupATK", fieldDto.getTurn_atk());
		retList.add(detailMap);

		//DBを更新
		fieldDao.update(fieldDto);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
