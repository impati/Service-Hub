package com.example.servicehub.domain.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.servicehub.domain.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "category")
@Entity
@Getter
@ToString(exclude = {"child", "parent"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "name", length = 1000, nullable = false, unique = true)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List<Category> child = new ArrayList<>();

	public void addChildCategory(final Category category) {
		this.child.add(category);
		category.parent = this;
	}

	public static Category of(final String name) {
		final Category category = new Category();
		category.name = name;
		return category;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o){
			return true;
		}
		if (!(o instanceof Category)){
			return false;
		}

		Category category = (Category)o;
		return this.getId() != null && Objects.equals(getId(), category.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
