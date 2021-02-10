package com.redhat.uxl.dataobjects.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redhat.uxl.commonjava.utils.IntegerUtils;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

/**
 * The type Person.
 */
@Data
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
  @Size(min = 1, max = 50)
  @Column(length = 50, unique = true)
  private String login;

  @JsonIgnore
  @Size(min = 5, max = 100)
  @Column(length = 100)
  private String password;

  @Size(max = 50)
  @Column(name = "first_name", length = 50)
  private String firstName;

  @Size(max = 50)
  @Column(name = "last_name", length = 50)
  private String lastName;

  @Email
  @Size(max = 100)
  @Column(length = 100, unique = true)
  private String email;

  @Column(nullable = false)
  private boolean activated = false;

  @Column(nullable = false)
  private boolean disabled = false;

  @Column(nullable = false)
  private boolean deleted = false;

  @Size(min = 2, max = 5)
  @Column(name = "lang_key", length = 5)
  private String langKey;

  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "person_authority",
      joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Authority> authorities = new HashSet<>();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "person")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<PersistentToken> persistentTokens = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Person person = (Person) o;

    if (!login.equals(person.login)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : IntegerUtils.ANSWER_TO_LIFE_THE_UNIVERSE_AND_EVERYTHING;
  }

  @Override
  public String toString() {
    return "Person{" + "login='" + login + '\'' + ", firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", activated='"
        + activated + '\'' + ", disabled='" + disabled + '\'' + ", langKey='" + langKey + '\''
        + "}";
  }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
    return String.format("%s %s", firstName, lastName);
  }
}
