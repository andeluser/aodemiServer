package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//神具「鼠」
public class m42 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		//自分のライフ＜相手のライフで処理を分ける
		BattleBaseDTO myBaseDao = baseDao.getAllValue(battleID, playerId);
		BattleBaseDTO enemyBaseDao = baseDao.getAllValue(battleID, enemyPlayerId);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();


		for (int i = 0; i < targetList.size(); i++) {

			BattleFieldDTO dto = fieldDtoList.get((int)targetList.get(i));

			//戻り値の作成
			HashMap<String, Object> detailMap = new HashMap<String, Object>();
			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", targetList.get(i));

			if (myBaseDao.getLife() <= enemyBaseDao.getLife()) {
				dto.setPermanent_hp(dto.getPermanent_hp() + 10);
				dto.setCur_hp(dto.getCur_hp() + 10);

				fieldDao.update(dto);

				detailMap.put("upHP", dto.getPermanent_hp());
				detailMap.put("hp", dto.getCur_hp());
			}

			if (myBaseDao.getLife() >= enemyBaseDao.getLife()) {
				dto.setPermanent_atk(dto.getPermanent_atk() + 10);
				dto.setPermanent_speed(dto.getPermanent_speed() + 1);

				fieldDao.update(dto);

				detailMap.put("upATK", dto.getPermanent_atk());
				detailMap.put("upAGI", dto.getPermanent_speed());
			}

			retList.add(detailMap);

		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
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
