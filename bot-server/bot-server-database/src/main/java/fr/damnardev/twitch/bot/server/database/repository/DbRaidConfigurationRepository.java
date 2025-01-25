package fr.damnardev.twitch.bot.server.database.repository;

import java.util.Optional;

import fr.damnardev.twitch.bot.server.database.entity.DbRaidConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DbRaidConfigurationRepository extends JpaRepository<DbRaidConfiguration, Long> {

	@Query("SELECT rc FROM DbRaidConfiguration rc JOIN FETCH rc.channel WHERE rc.channel.id = :id")
	Optional<DbRaidConfiguration> findByChannelId(@Param("id") Long id);

}
