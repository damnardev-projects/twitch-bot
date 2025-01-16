package fr.damnardev.twitch.bot.server.database.entity;

import java.time.OffsetDateTime;

import fr.damnardev.twitch.bot.model.GlobalCommandType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "t_channel_global_command", uniqueConstraints = { @jakarta.persistence.UniqueConstraint(columnNames = { "channel_id", "type" }) })
public class DbChannelGlobalCommand extends DbChannelCommand {

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private GlobalCommandType type;

	@Builder
	public DbChannelGlobalCommand(Long id, DbChannel channel, boolean enabled, String name, OffsetDateTime lastExecution, long cooldown, GlobalCommandType type) {
		super(id, channel, enabled, name, lastExecution, cooldown);
		this.type = type;
	}

}


