package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//唯一神の教え
public class m9 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleDeckDAO deckDao = factory.createDeckDAO();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		if (baseDto.getSpecial_stock() != 5) {
			return ret;
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO dto = fieldDtoList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 1) {

				BattleDeckDTO deckDto = deckDao.getAllValue(battleID, playerId, dto.getDeck_no());

				//対象のユニットを復活させる
				dto.setClose(0);
				dto.setAction(0);
				dto.setClose_number(0);
				dto.setOpen_close_number(0);

				//復活に伴う初期化処理
				if (deckDto.getClose_skill() == 1) {
					dto.setClose_skill(0);
				} else {
					dto.setClose_skill(1);
				}
				dto.setAction(0);
				dto.setPermanent_hp(0);
				dto.setTurn_hp(0);
				dto.setCur_hp(dto.getBase_hp());
				dto.setPermanent_level(0);
				dto.setTurn_level(0);
				dto.setPermanent_atk(0);
				dto.setTurn_atk(0);
				dto.setPermanent_def(0);
				dto.setTurn_def(0);
				dto.setPermanent_speed(0);
				dto.setTurn_speed(0);
				dto.setPermanent_range(0);
				dto.setTurn_range(0);

				fieldDao.update(dto);

				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("remove", "revive");
				retList.add(detailMap);
			}
		}

		if (retList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);


		//奥義ストックを０に設定する
		baseDto.setSpecial_stock(0);
		baseDao.update(baseDto);

		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList<Object> retList2 = new ArrayList<Object>();

		detailMap2.put("playerId", playerId);
		detailMap2.put("stock", 0);
		retList2.add(detailMap2);
		updateMap.put("special", retList2);

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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
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
