package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleFieldDAO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//高潔なる守護者
public class b16 implements CardAbility {

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
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();


		//戻り値の作成
		ArrayList<Object> retList = new ArrayList<Object>();

		if (fieldNumber > 5 && fieldNumber <= 8) {
			return ret;
		}

		int index = fieldNumber + 3;

		//後ろを強化する
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, index);

		//クローズは対象外
		if (fieldDto.getCard_id() != null && !"".equals(fieldDto.getCard_id()) && fieldDto.getClose() == 0) {
			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			//HPを20回復
			int maxhp = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();
			int hp = fieldDto.getCur_hp() + 20;

			if (hp >= maxhp) {
				hp = maxhp;
			}

			fieldDto.setCur_hp(hp);

			//防御を１０に
			int curDef = 10 - fieldDto.getCur_def();
			//ターン増加はゼロに
			fieldDto.setTurn_def(0);
			//永続増加は全体で１０となるように調整
			fieldDto.setPermanent_def(curDef);

			fieldDao.update(fieldDto);

			detailMap.put("hp", hp);
			detailMap.put("tupDEF", fieldDto.getTurn_def());
			detailMap.put("upDEF", fieldDto.getPermanent_def());
			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", index);
			retList.add(detailMap);
		} else {
			return ret;
		}


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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
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
		// TODO 自動生成されたメソッド・スタブ
		return null;

	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
