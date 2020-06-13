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

//荒くれ者
public class b71 implements CardAbility {

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

	//突撃号令
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

			//最大HP,現在HPを20増やす
			int perHp = fieldDto.getPermanent_hp() + 20;
			int hp = fieldDto.getCur_hp() + 20;

			//ATKを10増やす
			int perAtk =  fieldDto.getPermanent_atk() + 10;

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("hp", hp);
			detailMap.put("perHp", perHp);
			detailMap.put("perAtk", perAtk);

			fieldDto.setPermanent_atk(perAtk);
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

	//捨て身攻撃
	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
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

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//攻撃対象
			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);

			//ダメージ量を計算 防御は無視
			int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

			if (attack <= 0) {
				attack = 0;
			}

			//HP
			int enemyHp = enemyFieldDto.getCur_hp();

			//HPを計算 ダメージはATKの2倍
			enemyHp = enemyHp - attack*2;

			//相手のHPを設定
			enemyFieldDto.setCur_hp(enemyHp);

			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
			if (enemyHp <= 0) {
				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
			}

			fieldDao.update(enemyFieldDto);


			//戻り値を設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("playerId", enemyPlayerId);
			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
			updateList.add(updateMap);

			//自分のHP処理
			int hp = fieldDto.getCur_hp();
			int MaxHp = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();
			int damage = (MaxHp/2) - (fieldDto.getCur_def() + fieldDto.getPermanent_def() + fieldDto.getTurn_def());

			if (damage <= 0) {
				damage = 0;
			}

			//HPを計算
			hp = hp - damage;

			//自分のHPを設定
			fieldDto.setCur_hp(hp);

			//hpが0以下になったらクローズ
			if (hp <= 0) {
				fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
			}

			fieldDao.update(fieldDto);

			//戻り値を設定
			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("hp", hp);

			ArrayList<Object> mylist = new ArrayList<Object>();
			mylist.add(detailMap);

			HashMap<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("field", mylist);

			updateList.add(myMap);

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
