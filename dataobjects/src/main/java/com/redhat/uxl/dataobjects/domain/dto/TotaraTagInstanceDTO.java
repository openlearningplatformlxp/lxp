package com.redhat.uxl.dataobjects.domain.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * The type Totara tag instance dto.
 */
@Data
@Entity
@Table(name = "mdl_tag_instance")
public class TotaraTagInstanceDTO {

    /**
     * The Id.
     */
    @Column(length = 10)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  Long id;
    /**
     * The Tag id.
     */
    @Column(name = "tagid")
  Long tagId;
    /**
     * The Component.
     */
    @Column(name = "component")
  String component;
    /**
     * The Item type.
     */
    @Column(name = "itemtype")
  String itemType;
    /**
     * The Itemid.
     */
    @Column(name = "itemid")
  Long itemid;
    /**
     * The Tiuserid.
     */
    @Column(name = "tiuserid")
  Long tiuserid = 0l;
    /**
     * The Contextid.
     */
    @Column(name = "contextid")
  Long contextid = 45l;
    /**
     * The Ordering.
     */
    @Column(name = "ordering")
  Long ordering;

}
