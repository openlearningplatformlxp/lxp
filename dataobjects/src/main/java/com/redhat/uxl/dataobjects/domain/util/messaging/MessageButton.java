package com.redhat.uxl.dataobjects.domain.util.messaging;

import lombok.Data;

/**
 * The type Message button.
 */
@Data
public class MessageButton {
  private String text;
  private String action;
  private int order;

}
