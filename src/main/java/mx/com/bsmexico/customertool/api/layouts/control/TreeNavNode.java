package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.layout.Region;
import mx.com.bsmexico.customertool.api.layouts.control.FeatureMenuNavigator.NODETYPE;

/**
 * @author jchr
 *
 */
public class TreeNavNode {

	private TreeNavNode parent;
	private NODETYPE type;
	private List<TreeNavNode> children;
	private Region graphic;
	private Integer position;
	private String id;
	private Object elem;

	/**
	 * @param id
	 * @param parent
	 * @param children
	 * @param graphic
	 * @param position
	 */
	public TreeNavNode(final String id, final NODETYPE type, final TreeNavNode parent, final List<TreeNavNode> children,
			final Integer position) {
		this.parent = parent;
		this.type = (type == null) ? NODETYPE.NAV_NODE : type;
		this.children = (children == null) ? new ArrayList<>() : children;
		this.id = (StringUtils.isBlank(id)) ? UUID.randomUUID().toString() : id;
		this.position = (position == null) ? 0 : position;

	}

	/**
	 * @param id
	 * @param parent
	 * @param graphic
	 * @param position
	 */
	public TreeNavNode(final String id, final NODETYPE type, TreeNavNode parent, final Integer position) {
		this(id, type, parent, null, position);
	}

	/**
	 * @param id
	 * @param position
	 */
	public TreeNavNode(final String id, final NODETYPE type, final Integer position) {
		this(id, type, null, null, position);
	}

	/**
	 * @param child
	 */
	public void addChildren(final TreeNavNode child) {
		children.add(child);
	}

	/**
	 * @param node
	 */
	public void removeChildren(final TreeNavNode node) {
		children.remove(node);
	}

	/**
	 * @return immutable list of children
	 */
	public List<TreeNavNode> getChilden() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * @return immutable list of siblings
	 */
	public List<TreeNavNode> getSiblings() {
		return (parent == null || parent.children.isEmpty()) ? Collections.emptyList() : parent.getChilden();
	}

	/**
	 * Get Parent
	 * 
	 * @return the parent
	 */
	public TreeNavNode getParent() {
		return parent;
	}

	/**
	 * Change the parent
	 * 
	 * @param parent
	 * @return old Parent
	 */
	public TreeNavNode changeParent(TreeNavNode parent) {
		TreeNavNode oldParent = this.parent;
		this.parent = parent;
		return oldParent;
	}

	/**
	 * @return the graphic
	 */
	public Region getGraphic() {
		return graphic;
	}

	/**
	 * @param graphic the graphic to set
	 */
	public void setGraphic(Region graphic) {
		this.graphic = graphic;
	}

	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public Object getElement() {
		return elem;
	}

	/**
	 * @param
	 */
	public void bind(Object elem) {
		this.elem = elem;
	}

	/**
	 * @return the type
	 */
	public NODETYPE getType() {
		return type;
	}

	/**
	 * @return
	 */
	public int deep() {
		int deep = -1;
		if (parent == null) {
			// Root node
			deep = 0;
		} else {
			TreeNavNode currentParent = this.parent;
			deep = 0;			
			while (currentParent != null) {
				deep++;
				currentParent = currentParent.parent;
			}
		}
		return deep;
	}
}
