package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//太古の巨獣
public class m35 implements CardAbility {

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

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		//自分の情報を取得
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

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

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//どこの列か判定
			int start = 0;

			if (target == 1 || target == 4 || target == 7) {
				start = 1;
			} else if (target == 2 || target == 5 || target == 8) {
				start = 2;
			}

			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();

			int closeNumber = fieldDao.getCloseNumber(battleID) + 1;
			boolean closeFlg = false;

			for (int index = 0; index < 3; index++) {
				//攻撃対象のHPを減らす
				BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, start + (index * 3));

				if (enemyFieldDto.getCard_id() != null && !"".equals(enemyFieldDto.getCard_id()) && enemyFieldDto.getClose() == 0) {

					//ダメージ量を計算
					int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

					int def = enemyFieldDto.getTurn_def() + enemyFieldDto.getPermanent_def() + enemyFieldDto.getCur_def();

					attack = attack - def;

					if (attack < 0) {
						attack = 0;
					}

					int enemyHp = enemyFieldDto.getCur_hp();

					//HPを計算
					enemyHp = enemyHp - attack;

					//相手のHPを設定
					enemyFieldDto.setCur_hp(enemyHp);

					//相手のHPがゼロ以下となった場合はクローズ判定を立てる
					if (enemyHp <= 0) {
						enemyFieldDto.setClose(1);
						enemyFieldDto.setClose_number(closeNumber);
						closeFlg = true;
					}

					fieldDao.update(enemyFieldDto);

					//戻り値を設定
					HashMap<String, Object> updateMap = new HashMap<String, Object>();

					updateMap.put("fieldNumber", start + (index * 3));
					updateMap.put("hp", enemyHp);
					updateMap.put("playerId", enemyPlayerId);

					updateList.add(updateMap);
				}

			}

			if (closeFlg) {
				//後退した相手が居た場合はHPを２０回復
				int maxHp = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();

				int hp = fieldDto.getCur_hp() + 20;

				if (hp > maxHp) {
					hp = maxHp;
				}
				fieldDto.setCur_hp(hp);

				fieldDao.update(fieldDto);

				HashMap<String, Object> updateMap = new HashMap<String, Object>();

				updateMap.put("fieldNumber", fieldNumber);
				updateMap.put("hp", hp);
				updateMap.put("playerId", playerId);

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
