package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleFieldDAO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//ネクロマンサー『マンティ』
public class b33 implements CardAbility {

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
		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {

			BattleFieldDTO dto = fieldDtoList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && "黒".equals(dto.getColor())) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() == 0) {
			return ret;
		}

		HashMap<String, Object> targetMap = new HashMap<String, Object>();
		targetMap.put("list", targetList);
		targetMap.put("playerId", playerId);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(targetMap);

		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		//戻り値設定
		ret.put("updateInfo", new HashMap<String, Object>());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		HashMap<String, Object> ret = new HashMap<String, Object>();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		BattleFieldDTO myDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//自身のATKを選択した対象のfrm×10増やす
					int frm = fieldDto.getPermanent_frm() + fieldDto.getTurn_frm() + fieldDto.getCur_frm();
					myDto.setTurn_atk(myDto.getTurn_atk() +  (frm * 10));
					fieldDao.update(myDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", playerId);
					detailMap.put("fieldNumber", fieldNumber);
					detailMap.put("tupATK", myDto.getTurn_atk());
					retList.add(detailMap);

					//選択した対象のATKとAGIをプラスする
					fieldDto.setTurn_atk(fieldDto.getTurn_atk() + 10);
					fieldDto.setTurn_speed(fieldDto.getTurn_speed() + 1);
					fieldDao.update(fieldDto);

					HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
					detailMap2.put("playerId", player1);
					detailMap2.put("fieldNumber", list.get(k));
					detailMap2.put("tupATK", fieldDto.getTurn_atk());
					detailMap2.put("tupAGI", fieldDto.getTurn_speed());
					retList.add(detailMap2);

				}
			}
		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
