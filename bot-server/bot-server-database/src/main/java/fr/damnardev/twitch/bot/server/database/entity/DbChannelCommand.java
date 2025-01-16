package fr.damnardev.twitch.bot.server.database.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DbChannelCommand {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_channel_command")
	@SequenceGenerator(name = "s_channel_command", sequenceName = "s_channel_command", allocationSize = 1)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "channel_id")
	@ToString.Exclude
	private DbChannel channel;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "name")
	private String name;

	@Column(name = "last_execution", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime lastExecution;

	@Column(name = "cooldown")
	private long cooldown;

}
