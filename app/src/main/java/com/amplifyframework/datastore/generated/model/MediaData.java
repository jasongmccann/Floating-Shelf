package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the MediaData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "MediaData", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class MediaData implements Model {
  public static final QueryField ID = field("MediaData", "id");
  public static final QueryField NAME = field("MediaData", "name");
  public static final QueryField PLATFORM = field("MediaData", "platform");
  public static final QueryField YEAR = field("MediaData", "year");
  public static final QueryField FORMAT = field("MediaData", "format");
  public static final QueryField RATING = field("MediaData", "rating");
  public static final QueryField PLAYSTATUS = field("MediaData", "playstatus");
  public static final QueryField IMAGE = field("MediaData", "image");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String") String platform;
  private final @ModelField(targetType="String") String year;
  private final @ModelField(targetType="String") String format;
  private final @ModelField(targetType="String") String rating;
  private final @ModelField(targetType="String") String playstatus;
  private final @ModelField(targetType="String") String image;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public String getPlatform() {
      return platform;
  }
  
  public String getYear() {
      return year;
  }
  
  public String getFormat() {
      return format;
  }
  
  public String getRating() {
      return rating;
  }
  
  public String getPlaystatus() {
      return playstatus;
  }
  
  public String getImage() {
      return image;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private MediaData(String id, String name, String platform, String year, String format, String rating, String playstatus, String image) {
    this.id = id;
    this.name = name;
    this.platform = platform;
    this.year = year;
    this.format = format;
    this.rating = rating;
    this.playstatus = playstatus;
    this.image = image;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      MediaData mediaData = (MediaData) obj;
      return ObjectsCompat.equals(getId(), mediaData.getId()) &&
              ObjectsCompat.equals(getName(), mediaData.getName()) &&
              ObjectsCompat.equals(getPlatform(), mediaData.getPlatform()) &&
              ObjectsCompat.equals(getYear(), mediaData.getYear()) &&
              ObjectsCompat.equals(getFormat(), mediaData.getFormat()) &&
              ObjectsCompat.equals(getRating(), mediaData.getRating()) &&
              ObjectsCompat.equals(getPlaystatus(), mediaData.getPlaystatus()) &&
              ObjectsCompat.equals(getImage(), mediaData.getImage()) &&
              ObjectsCompat.equals(getCreatedAt(), mediaData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), mediaData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getPlatform())
      .append(getYear())
      .append(getFormat())
      .append(getRating())
      .append(getPlaystatus())
      .append(getImage())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("MediaData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("platform=" + String.valueOf(getPlatform()) + ", ")
      .append("year=" + String.valueOf(getYear()) + ", ")
      .append("format=" + String.valueOf(getFormat()) + ", ")
      .append("rating=" + String.valueOf(getRating()) + ", ")
      .append("playstatus=" + String.valueOf(getPlaystatus()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static MediaData justId(String id) {
    return new MediaData(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      platform,
      year,
      format,
      rating,
      playstatus,
      image);
  }
  public interface NameStep {
    BuildStep name(String name);
  }
  

  public interface BuildStep {
    MediaData build();
    BuildStep id(String id);
    BuildStep platform(String platform);
    BuildStep year(String year);
    BuildStep format(String format);
    BuildStep rating(String rating);
    BuildStep playstatus(String playstatus);
    BuildStep image(String image);
  }
  

  public static class Builder implements NameStep, BuildStep {
    private String id;
    private String name;
    private String platform;
    private String year;
    private String format;
    private String rating;
    private String playstatus;
    private String image;
    @Override
     public MediaData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new MediaData(
          id,
          name,
          platform,
          year,
          format,
          rating,
          playstatus,
          image);
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep platform(String platform) {
        this.platform = platform;
        return this;
    }
    
    @Override
     public BuildStep year(String year) {
        this.year = year;
        return this;
    }
    
    @Override
     public BuildStep format(String format) {
        this.format = format;
        return this;
    }
    
    @Override
     public BuildStep rating(String rating) {
        this.rating = rating;
        return this;
    }
    
    @Override
     public BuildStep playstatus(String playstatus) {
        this.playstatus = playstatus;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        this.image = image;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, String platform, String year, String format, String rating, String playstatus, String image) {
      super.id(id);
      super.name(name)
        .platform(platform)
        .year(year)
        .format(format)
        .rating(rating)
        .playstatus(playstatus)
        .image(image);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder platform(String platform) {
      return (CopyOfBuilder) super.platform(platform);
    }
    
    @Override
     public CopyOfBuilder year(String year) {
      return (CopyOfBuilder) super.year(year);
    }
    
    @Override
     public CopyOfBuilder format(String format) {
      return (CopyOfBuilder) super.format(format);
    }
    
    @Override
     public CopyOfBuilder rating(String rating) {
      return (CopyOfBuilder) super.rating(rating);
    }
    
    @Override
     public CopyOfBuilder playstatus(String playstatus) {
      return (CopyOfBuilder) super.playstatus(playstatus);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
  }
  
}
