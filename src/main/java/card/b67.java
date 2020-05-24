package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//大鎌の主
public class b67 implements CardAbility {

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

	//急成長
	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		int special_use = baseDto.getSpecial_use();
		int special_use_bye_skill = baseDto.getSpecial_use_bye_skill();

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//このターンに奥義使用のフラグが立っていなければ処理終了
		if(special_use != 1 && special_use_bye_skill != 1) {
			return new HashMap<String, Object>();
		}
			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);

			//最大HP,現在HPを30増やす
			int perHp = fieldDto.getPermanent_hp() + 30;
			int hp = fieldDto.getCur_hp() + 30;

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("hp", hp);
			detailMap.put("perHp", perHp);

			fieldDto.setPermanent_hp(perHp);
			fieldDto.setCur_hp(hp);
			fieldDao.update(fieldDto);

			retList.add(detailMap);

			HashMap<String, Object> updateMap = new HashMap<String, Object>();
			ArrayList<Object> updateList = new ArrayList<Object>();

			updateMap.put("field", retList);
			updateList.add(updateMap);

			ret.put("updateInfo", updateList);
			ret.put("target", new ArrayList<Object>());



		return ret;
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

	//薙ぎ払い from_b12
	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		//SPを２消費
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		baseDto.setSp(baseDto.getSp() - 2);
		baseDao.update(baseDto);

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		//自分の情報を取得
		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> tmpList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		//対象なし
		if (tmpList.size() == 0) {
			return ret;
		}

		Random rand = new Random();
		int num = rand.nextInt(tmpList.size());

		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, enemyPlayerId);
		BattleFieldDTO myDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//どの列かを判定し処理する

		int target = (int)tmpList.get(num);

		int startRowNumber = 0;

		if (target <= 2) {
			startRowNumber = 0;
		} else if (target > 2 && target <= 5) {
			startRowNumber = 3;
		} else if (target > 5 && target <= 8) {
			startRowNumber = 6;
		}

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//複数クローズの可能性があるので番号を取得する
		int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

		for (int i = 0; i < 3; i++) {
			int index  = startRowNumber + i;
			BattleFieldDTO fieldDto = fieldList.get(index);

			if (fieldDto.getCard_id() != null && !"".equals(fieldDto.getCard_id()) && fieldDto.getClose() == 0) {

				//対象にダメージ
				int hp = fieldDto.getCur_hp();

				int attack = myDto.getPermanent_atk() + myDto.getTurn_atk() + myDto.getCur_atk();
				attack = attack - fieldDto.getPermanent_def() - fieldDto.getTurn_def() - fieldDto.getCur_def();

				if (attack <= 0) {
					attack = 0;
				}

				hp = hp - attack;

				fieldDto.setCur_hp(hp);

				//Hpが１以下ならクローズ情報を設定
				if (fieldDto.getCur_hp() <= 0) {
					fieldDto.setClose(1);
					fieldDto.setClose_number(closeNumber);
				}

				fieldDao.update(fieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", index);
				detailMap.put("hp", hp);
				retList.add(detailMap);
			}
		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	//捕食準備
	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);

		//現在HPを最大HPの25%分増やす 端数切捨て
		int maxHp = fieldDto.getBase_hp() + fieldDto.getTurn_hp() + fieldDto.getPermanent_hp();
		int hp = fieldDto.getCur_hp() + (maxHp/4);

		if (hp >= maxHp) {
			hp = maxHp;
		}

		//ATKを5増やす
		int perAtk =  fieldDto.getPermanent_atk() + 5;


		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("hp", hp);
		detailMap.put("perAtk", perAtk);

		fieldDto.setPermanent_atk(perAtk);
		fieldDto.setCur_hp(hp);
		fieldDao.update(fieldDto);

		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;


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
