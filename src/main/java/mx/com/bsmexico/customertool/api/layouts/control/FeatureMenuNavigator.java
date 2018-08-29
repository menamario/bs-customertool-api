package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.layout.Region;
import mx.com.bsmexico.customertool.api.Feature;
import mx.com.bsmexico.customertool.api.MenuNavigator;
import mx.com.bsmexico.customertool.api.NavRoute;

/**
 * 
 * 
 * @author jchr
 *
 */
public abstract class FeatureMenuNavigator extends MenuNavigator {

	private List<Feature> components;
	
	protected final String ROOT_NAME = "ROOT";
	protected final String DEFAULT_SECTION_NAME = "DEFAULT";
	protected TreeNavNode root;

	/**
	 * @param components
	 * @throws IllegalArgumentException
	 */
	public FeatureMenuNavigator(final List<Feature> components) throws IllegalArgumentException {
		if (components == null || components.isEmpty()) {
			throw new IllegalArgumentException("List components can not be empty or null");
		}
		this.components = components;
		final List<NavRoute> routes = new ArrayList<>();
		this.components.forEach(cmp -> {
			cmp.setMenuNavigator(this);
			if (cmp.getLayout() != null && cmp.getLayout().getRoute() != null) {
				routes.add(cmp.getLayout().getRoute());
			}
		});
		builLogicNavigationTree(routes);
		bindComponents();
	}



	/**
	 * Bind a ComponentLayout to Tree Nodes
	 */
	private void bindComponents() {
		if (this.root != null) {
			components.forEach(cmp -> bindComponent(cmp));
		}
	}

	/**
	 * @param component
	 */
	private void bindComponent(final Feature component) {
		if (component != null && component.getLayout() != null && component.getLayout().getRoute() != null) {
			final NavRoute route = component.getLayout().getRoute();			
			List<TreeNavNode> branch = this.getBranchByRoute(root, route);
			if(!branch.isEmpty()) {
				//Bind component to leaf of the branch
				branch.get(branch.size() -1).bind(component);
			}
			
		}
	}
	
	/**
	 * Node types in the logic navigation tree
	 * 
	 * @author jchr
	 *
	 */
	public enum NODETYPE {
		SECTION_NODE, NAV_NODE, ROOT_NODE, LEAF_NODE;
	}

	/**
	 * Build a Navigation Tree Structure from the Component Layouts
	 * 
	 * @param components
	 * @return
	 */


	/**
	 * Build a branch in the logic tree
	 * 
	 * 
	 * @param parent
	 * @param route
	 */
	protected void buildTreeBranch(final TreeNavNode parent, final NavRoute route, boolean section) {
		if (parent != null && route != null) {
			if (route.hasNext()) {
				final String name = (StringUtils.isBlank(route.getSection())) ? DEFAULT_SECTION_NAME
						: route.getSection();
				final NavRoute.NavNode node = (section) ? new NavRoute.NavNode(name, name, null, 0, false)
						: route.next();
				TreeNavNode nextParent = null;
				final Optional<TreeNavNode> optNode = parent.getChilden().stream()
						.filter(child -> child.getId() == node.getName()).findFirst();
				if (optNode.isPresent()) {
					nextParent = optNode.get();
				} else {
					NODETYPE type = (route.hasNext()) ? ((section) ? NODETYPE.SECTION_NODE : NODETYPE.NAV_NODE)
							: NODETYPE.LEAF_NODE;
					nextParent = new TreeNavNode(node.getName(), type, parent, node.getPosition());
					nextParent.setGraphic(getGraphicNavigatorNode(node, type));
					parent.addChildren(nextParent);
				}
				buildTreeBranch(nextParent, route, false);
			}
		}
	}

	/**
	 * Find a specific node in the logic tree
	 * 
	 * @param node
	 * @param id
	 * @return the first node with id
	 */
	protected TreeNavNode findTreeNode(final TreeNavNode node, final String id) {
		TreeNavNode result = null;
		if (node != null || !StringUtils.isBlank(id)) {
			if (node.getId().equals(id)) {
				result = node;
			} else {
				List<TreeNavNode> children = node.getChilden();
				for (TreeNavNode child : children) {
					result = findTreeNode(child, id);
					if (result != null) {
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Get all branch corresponding to the route
	 * 
	 * @param root
	 * @param route
	 * @return
	 */
	protected List<TreeNavNode> getBranchByRoute(final TreeNavNode root, final NavRoute route) {
		List<TreeNavNode> branch = new ArrayList<>();
		route.goStart();
		TreeNavNode currentNode = root;
		while (route.hasNext() && currentNode != null) {
			NavRoute.NavNode navNode = route.next();
			currentNode = findTreeNode(currentNode, navNode.getName());
			branch.add(currentNode);
		}
		return branch;
	}
	
	protected void builLogicNavigationTree(final List<NavRoute> routes) {
		// TODO agregar graphic
		root = new TreeNavNode(ROOT_NAME, NODETYPE.ROOT_NODE, null, 0);
		root.setGraphic(getGraphicNavigatorNode(null, NODETYPE.ROOT_NODE));
		if (routes != null) {
			routes.forEach(r -> this.buildTreeBranch(root, r, true));
		}

	}

	/**
	 * 
	 * Create the graphic part corresponding to a node in the Navigator
	 * 
	 * @param navNode
	 * @return
	 */
	protected abstract Region getGraphicNavigatorNode(NavRoute.NavNode navNode, NODETYPE type);
}
