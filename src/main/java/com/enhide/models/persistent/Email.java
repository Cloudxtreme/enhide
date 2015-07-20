package com.enhide.models.persistent;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author sales@enhide.com
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@objectId")
public class Email extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "email_address", joinColumns = {
		@JoinColumn(name = "email_id")}, inverseJoinColumns = {
		@JoinColumn(name = "address_id")})
	@NotEmpty
	private Set<Address> froms = new HashSet<Address>();
	//http://serverfault.com/questions/554520/smtp-allows-for-multiple-from-addresses-in-the-rfc-was-this-ever-useful-why-do

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "email_address", joinColumns = {
		@JoinColumn(name = "email_id")}, inverseJoinColumns = {
		@JoinColumn(name = "address_id")})
	@NotEmpty
	private Set<Address> tos = new HashSet<Address>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "email_address", joinColumns = {
		@JoinColumn(name = "email_id")}, inverseJoinColumns = {
		@JoinColumn(name = "address_id")})
	private Set<Address> ccs = new HashSet<Address>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "email_address", joinColumns = {
		@JoinColumn(name = "email_id")}, inverseJoinColumns = {
		@JoinColumn(name = "address_id")})
	private Set<Address> bccs = new HashSet<Address>();

	@NotEmpty
	private String subject;

	@Lob
	private String message;

	@Lob
	private String clearText;

	@Lob
	private String signature;

	@Lob
	private String other;

	@ManyToOne
	private Email replyTo;

	@OneToMany(mappedBy = "replyTo")
	private Set<Email> replies;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Address> getFroms() {
		return froms;
	}

	public void setFroms(Set<Address> froms) {
		this.froms = froms;
	}

	public Email getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Email replyTo) {
		this.replyTo = replyTo;
	}

	public Set<Email> getReplies() {
		return replies;
	}

	public void setReplies(Set<Email> replies) {
		this.replies = replies;
	}

	public Set<Address> getTos() {
		return tos;
	}

	public void setTos(Set<Address> tos) {
		this.tos = tos;
	}

	public Set<Address> getCcs() {
		return ccs;
	}

	public void setCcs(Set<Address> ccs) {
		this.ccs = ccs;
	}

	public Set<Address> getBccs() {
		return bccs;
	}

	public void setBccs(Set<Address> bccs) {
		this.bccs = bccs;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getClearText() {
		return clearText;
	}

	public void setClearText(String clearText) {
		this.clearText = clearText;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
}
