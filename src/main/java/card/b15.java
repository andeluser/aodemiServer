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

		//自分の情報を取得
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> tmpList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		ArrayList<Object> targetList = new ArrayList<Object>();
		ArrayList<BattleFieldDTO> dtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//DEFが０以上のユニットが対象
		for (int i = 0; i < tmpList.size(); i++) {

			int number = Integer.parseInt(tmpList.get(i).toString());

			BattleFieldDTO dto = dtoList.get(number);

			int def = dto.getPermanent_def() + dto.getTurn_def() + dto.getCur_def();

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && def > 0 ) {
				targetList.add(number);
			}
		}

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();

			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);

			//ダメージ量を計算
			int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();
			attack = attack * 2;

			int enemyHp = enemyFieldDto.getCur_hp();

			//HPを計算
			enemyHp = enemyHp - attack;

			//相手のHPを設定
			enemyFieldDto.setCur_hp(enemyHp);

			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
			if (enemyHp <= 0) {
				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
			}

			//DEFをゼロにする
			int def = enemyFieldDto.getPermanent_def() + enemyFieldDto.getCur_def();

			enemyFieldDto.setPermanent_def(def * -1);
			enemyFieldDto.setTurn_def(0);

			fieldDao.update(enemyFieldDto);

			//戻り値を設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("tupDEF", 0);
			updateMap.put("upDEF", enemyFieldDto.getPermanent_def());
			updateMap.put("playerId", enemyPlayerId);

			updateList.add(updateMap);

			HashMap<String, Object> orderMap = new HashMap<String, Object>();
			ArrayList<Object> orderList = new ArrayList<Object>();

			orderMap.put("field", updateList);
			orderList.add(orderMap);

			ret.put("updateInfo", orderList);

		}

		return ret;
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
