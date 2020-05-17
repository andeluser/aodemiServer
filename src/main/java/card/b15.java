package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleFieldDAO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//精鋭神官
public class b15 implements CardAbility {

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

		//自分の列を確認
		int startNumber = 0;

		if (fieldNumber <= 2) {
			startNumber = 0;
		} else if (fieldNumber > 2 && fieldNumber <= 5) {
			startNumber = 3;
		} else if (fieldNumber > 5 && fieldNumber <= 8) {
			startNumber = 6;
		}

		for (int i = 0; i < 3; i++) {

			if (fieldNumber == 0 || fieldNumber == 3 || fieldNumber == 6) {
				//左陣の場合は右陣は対象外
				if (i == 2) {
					continue;
				}
			} else if (fieldNumber == 2 || fieldNumber == 5 || fieldNumber == 8) {
				//右陣の場合は左陣は対象外
				if (i == 0) {
					continue;
				}
			}

			int index = startNumber + i;
			BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, index);

			//クローズは対象外
			if (fieldDto.getCard_id() != null && !"".equals(fieldDto.getCard_id()) && fieldDto.getClose() == 0) {
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				int maxhp = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();
				int hp = fieldDto.getCur_hp() + 15;

				if (hp >= maxhp) {
					hp = maxhp;
				}

				fieldDto.setCur_hp(hp);
				//自分の左右のHPを15回復
				fieldDao.update(fieldDto);

				detailMap.put("hp", hp);
				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", index);
				retList.add(detailMap);
			}

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
