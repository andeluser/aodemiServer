package dao;

import java.util.ArrayList;

import dto.BattleDeckDTO;

public interface BattleDeckDAO {

	BattleDeckDTO getAllValue(String battleID, String playerId, int deckNumber) throws Exception;
	ArrayList<BattleDeckDTO> getAllList(String battleID, String playerId) throws Exception;
	int insert(BattleDeckDTO dto) throws Exception;
	int deleteBattle(BattleDeckDTO dto) throws Exception;
	int update(BattleDeckDTO dto) throws Exception;
}
