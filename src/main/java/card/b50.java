package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import factory.DaoFactory;

//高等魔術師
public class b50 implements CardAbility {

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
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);
		BattleDeckDAO deckDao = factory.createDeckDAO();

		//ＳＰを2減らす
		baseDto.setSp(baseDto.getSp() - 2);
		baseDao.update(baseDto);

		//相手プレイヤーのID取得
		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//相手のデッキに残っているカード取得
		ArrayList<Object> retList = new ArrayList<Object>();
		ArrayList<BattleDeckDTO> deckDtoList = deckDao.getAllList(battleID, enemyPlayerId);
		ArrayList<Integer> deckList = new ArrayList<Integer>();

		//存在している魔法カードをリストへ
		for (int i = 0; i < deckDtoList.size(); i++) {
			BattleDeckDTO deckDto = deckDtoList.get(i);
//			if (deckDto.getCard_out() == 0 && deckDto.getCard_type() == "魔法") {
				deckList.add(deckDto.getDeck_no());
//			}
		}

		//対象がない場合は処理終了
		if(deckList.size() == 0) {
			return new HashMap<String, Object>();
		}

		//ランダムに1枚選ぶ
		Random random = new Random();
		int selectDeck_no = deckList.get(random.nextInt(deckList.size()));

		//ロックする ！ターン数要確認
		BattleDeckDTO deckDto = deckDtoList.get(selectDeck_no);
		deckDto.setCard_lock(1);
		deckDao.update(deckDto);

		//戻り値
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		detailMap.put("playerId", enemyPlayerId);
		detailMap.put("deckNumber", selectDeck_no);
		detailMap.put("lockTurn", 1);
		retList.add(detailMap);

		updateMap.put("deckLock", retList);

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
