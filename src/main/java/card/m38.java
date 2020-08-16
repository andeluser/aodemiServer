package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.CommonUtil;

//煉獄の炎
public class m38 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();

		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}



		ArrayList<Object> updateList = new ArrayList<Object>();

		for (int index = 0; index < 3; index++) {

			//相手の対象計算
			ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);
			//対象を計算する
			ArrayList<Object> enemyTargetList = new ArrayList<Object>();

			for (int i = 0; i < enemyFieldDtoList.size(); i++) {
				BattleFieldDTO list = enemyFieldDtoList.get(i);

				if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
					enemyTargetList.add(i);
				}
			}

			if (enemyTargetList.size() == 0) {
				//対象が一人も居ない場合は処理終了
				return new HashMap<String, Object>();
			}

			List result = new ArrayList();

			//対象からランダムで１体選択する
			Collections.shuffle(enemyTargetList);
			result = enemyTargetList.subList(0, 1);

			ArrayList<Object> list = new ArrayList<Object>();

			for (int i = 0; i < result.size(); i++) {

				BattleFieldDTO dto = enemyFieldDtoList.get((int)result.get(i));
				int hp = dto.getCur_hp();
				int def = dto.getPermanent_def() + dto.getTurn_def() + dto.getCur_def();

				int attack = 30 - def;

				if (attack < 0) {
					attack = 0;
				}

				hp = hp - attack;

				dto.setCur_hp(hp);

				if (hp <= 0) {
					fieldDao.setClose(battleID, dto.getPlayer_id(), dto.getField_no(), dto);
				}
				fieldDao.update(dto);

				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", result.get(i));
				detailMap.put("hp", hp);
				list.add(detailMap);
			}

			//戻り値設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("field", list);
			updateList.add(updateMap);

		}

		//相手の奥義ストックを１増やす
		HashMap<String, Object> specialDetailMap = new HashMap<String, Object>();
		ArrayList<Object> specialList = new ArrayList<Object>();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);

		CommonUtil util = new CommonUtil();

		HashMap<String, Object> map = util.addSpecial(enemyBaseDto.getSpecial_gage(), enemyBaseDto.getSpecial_stock(), 0, 1);
		enemyBaseDto.setSpecial_gage((int)map.get("gage"));
		enemyBaseDto.setSpecial_stock((int)map.get("stock"));
		baseDao.update(enemyBaseDto);

		specialDetailMap.put("playerId", enemyPlayerId);
		specialDetailMap.put("gage", enemyBaseDto.getSpecial_gage());
		specialDetailMap.put("stock", enemyBaseDto.getSpecial_stock());
		specialList.add(specialDetailMap);

		//自分の奥義ストックを２減らす
		HashMap<String, Object> specialDetailMap2 = new HashMap<String, Object>();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		HashMap<String, Object> map2 = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), 0, -2);
		baseDto.setSpecial_gage((int)map2.get("gage"));
		baseDto.setSpecial_stock((int)map2.get("stock"));
		baseDao.update(baseDto);

		specialDetailMap2.put("playerId", playerId);
		specialDetailMap2.put("gage", baseDto.getSpecial_gage());
		specialDetailMap2.put("stock", baseDto.getSpecial_stock());
		specialList.add(specialDetailMap2);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("special", specialList);
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
