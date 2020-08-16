package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleFieldDAO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//死霊将軍『グラフト』
public class b39 implements CardAbility {

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
		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, playerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		//自分のユニットのDEFをターン中２０上げる
		for (int i = 0; i < fieldList.size(); i++) {

			BattleFieldDTO dto = fieldList.get(i);

			if (!"".equals(dto.getCard_id()) && dto.getClose() == 0 && "黒".equals(dto.getColor())) {
				int frm = dto.getPermanent_frm() + dto.getTurn_frm() + dto.getCur_frm();

				dto.setTurn_atk(dto.getTurn_atk() + (frm * 5));
				fieldDao.update(dto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("tupATK", fieldList.get(i).getTurn_atk());
				retList.add(detailMap);
			}
		}

		if (retList.size() == 0) {
			return ret;
		}

		fieldDao.update(fieldList);

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
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
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
