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
import util.BattleFieldUtil;

//白狼『ハクア』
public class m2 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
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

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		for (int k= 0; k < targetList.size(); k++) {
			BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, (int)targetList.get(k));

			//対象のユニットのターンDEF＋３０、アクション終了する
			fieldDto.setTurn_def(fieldDto.getTurn_def() + 30);
			fieldDto.setAction(1);

			fieldDao.update(fieldDto);

			//戻り値の作成
			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", targetList.get(k));
			detailMap.put("tupDEF", fieldDto.getTurn_def());
			detailMap.put("remove", "actionEnd");
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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ＳＰを１減らす
		baseDto.setSp(baseDto.getSp() - 1);
		baseDao.update(baseDto);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		if (targetList.size() != 0) {

			//自分の味方ユニットの数だけダメージ
			ArrayList<BattleFieldDTO> myfieldList = fieldDao.getAllList(battleID, playerId);
			int dmg = 0;

			for (int i = 0; i < myfieldList.size(); i++) {
				BattleFieldDTO dto = myfieldList.get(i);

				if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
					dmg = dmg + 10;
				}
			}

			int attack = dmg;

			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();

			int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

			for (int i = 0; i < targetList.size(); i++) {
				//攻撃対象のHPを減らす
				BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, (int)targetList.get(i));

				int def = enemyFieldDto.getTurn_def() + enemyFieldDto.getPermanent_def() + enemyFieldDto.getCur_def();

				attack = attack - def;

				if (attack < 0) {
					attack = 0;
				}

				int enemyHp = enemyFieldDto.getCur_hp() - attack;

				//相手のHPを設定
				enemyFieldDto.setCur_hp(enemyHp);

				//相手のHPがゼロ以下となった場合はクローズ判定を立てる。複数クローズする可能性あり
				if (enemyHp <= 0) {
					enemyFieldDto.setClose(1);
					enemyFieldDto.setClose_number(closeNumber);
				}

				fieldDao.update(enemyFieldDto);

				//戻り値を設定
				HashMap<String, Object> updateMap = new HashMap<String, Object>();

				updateMap.put("fieldNumber", enemyFieldDto.getField_no());
				updateMap.put("hp", enemyHp);
				updateMap.put("playerId", enemyPlayerId);

				updateList.add(updateMap);
			}

			HashMap<String, Object> orderMap = new HashMap<String, Object>();
			ArrayList<Object> orderList = new ArrayList<Object>();

			orderMap.put("field", updateList);
			orderList.add(orderMap);

			ret.put("updateInfo", orderList);

		}

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
