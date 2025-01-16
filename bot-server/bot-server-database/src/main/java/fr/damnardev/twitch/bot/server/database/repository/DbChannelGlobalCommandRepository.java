package fr.damnardev.twitch.bot.server.database.repository;

import java.util.List;
import java.util.Optional;

import fr.damnardev.twitch.bot.server.database.entity.DbChannelGlobalCommand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DbChannelGlobalCommandRepository extends JpaRepository<DbChannelGlobalCommand, Long> {

	@Query("SELECT cc FROM DbChannelGlobalCommand cc JOIN FETCH cc.channel WHERE upper(cc.channel.name) = upper(:channelName)")
	List<DbChannelGlobalCommand> findByChannelName(@Param("channelName") String channelName);

	@Query("SELECT cc FROM DbChannelGlobalCommand cc JOIN FETCH cc.channel WHERE upper(cc.channel.name) = upper(:channelName) AND upper(cc.name) = upper(:name)")
	Optional<DbChannelGlobalCommand> findByChannelNameAndName(@Param("channelName") String channelName, @Param("name") String name);

}
