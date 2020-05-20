package shield;

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

//宿り木の開花
public class b88 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		//自分のSPを１増やす
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		baseDto.setSp(baseDto.getSp() + 1);
		baseDao.update(baseDto);

		ArrayList<Object> list = new ArrayList<Object>();

		HashMap<String, Object> spMap = new HashMap<String, Object>();

		spMap.put("playerId", playerId);
		spMap.put("SP", baseDto.getSp());
		list.add(spMap);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO dto = enemyFieldDtoList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
				enemyTargetList.add(i);
			}
		}

		if (enemyTargetList.size() != 0) {
			//対象からランダムで１体選択する
			List result = new ArrayList();
			Collections.shuffle(enemyTargetList);
			result = enemyTargetList.subList(0, 1);

			for (int i = 0; i < result.size(); i++) {

				BattleFieldDTO dto = enemyFieldDtoList.get((int)result.get(i));
				int hp = dto.getCur_hp();
				int def = dto.getPermanent_def() + dto.getTurn_def() + dto.getCur_def();

				int attack = 40 - def;

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
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", list);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> shieldSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
