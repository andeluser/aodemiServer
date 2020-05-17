package special;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dao.BattleBaseDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//自由自在
public class o8 implements SpecialAbility {

	@Override
	public HashMap<String, Object> specialSkill(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, playerId);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストックを３消費
		baseDto.setSpecial_stock(baseDto.getSpecial_stock() - 4);

		ArrayList<Object> retList = new ArrayList<Object>();

		if (baseDto.getCemetery() != null && !"".equals(baseDto.getCemetery())) {

			//候補から対象を決める
			String[] cemeteryList = baseDto.getCemetery().split(",");

			List<String> list = Arrays.asList(cemeteryList);
			StringBuilder sb = new StringBuilder();
			List result = new ArrayList();

			if (list.size() > 3) {

				ArrayList updateList = new ArrayList();
				List<String> shuffled = new ArrayList<String>(list);
				Collections.shuffle(shuffled);
				result = shuffled.subList(0, 3);

				//対象以外は墓地から除外する
				for (int i = 0; i < cemeteryList.length; i++) {
					if (!result.contains(cemeteryList[i])) {
						updateList.add(cemeteryList[i]);
					}
				}

				for (int i = 0; i < updateList.size(); i++) {
					if (i != 0) {
						sb.append(",");
					}
					sb.append(updateList.get(i));
				}
			} else {
				result = list;
			}

			baseDto.setCemetery(sb.toString());

			//戻り値の作成
			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			detailMap.put("playerId", playerId);
			detailMap.put("cemeteryNumber", result);

			retList.add(detailMap);
		}

		baseDao.update(baseDto);

		if (retList.size() == 0) {
			return ret;
		}

		fieldDao.update(fieldList);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("repair", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> specialSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


}
