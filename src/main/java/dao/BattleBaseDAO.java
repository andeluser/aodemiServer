package dao;

import dto.BattleBaseDTO;

public interface BattleBaseDAO {

	BattleBaseDTO getAllValue(String battleID, String playerId) throws Exception;
	int insert(BattleBaseDTO dto) throws Exception;
	int deleteBattle(BattleBaseDTO dto) throws Exception;
	int update(BattleBaseDTO dto) throws Exception;
}
