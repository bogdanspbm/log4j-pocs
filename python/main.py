import pydot
from collections import deque


def read_file_to_set(filename):
    with open(filename, 'r') as file:
        return set(line.strip() for line in file.readlines())


def get_node(graph, node_name):
    nodes = graph.get_node(node_name)
    if nodes:
        return nodes[0]
    return None


def find_ancestors(edges, node_name):
    ancestors = set()
    queue = deque([node_name])
    while queue:
        current = queue.popleft()
        for edge in edges:
            if edge.get_destination().strip('"') == current:
                source = edge.get_source().strip('"')
                if source not in ancestors:
                    ancestors.add(source)
                    queue.append(source)
    return ancestors


def find_descendants(edges, node_name):
    descendants = set()
    queue = deque([node_name])
    while queue:
        current = queue.popleft()
        for edge in edges:
            if edge.get_source().strip('"') == current:
                dest = edge.get_destination().strip('"')
                if dest not in descendants:
                    descendants.add(dest)
                    queue.append(dest)
    return descendants


def set_color(node, color):
    node.set_style("filled")
    node.set_fillcolor(color)


def color_graph(dot_filename, poc_file, not_proven_file, abstract_file, output_dot_filename, output_svg_filename):
    # Загрузка файлов
    poc_methods = read_file_to_set(poc_file)
    not_proven_methods = read_file_to_set(not_proven_file)
    abstract_methods = read_file_to_set(abstract_file)

    # Загрузка графа
    (graph,) = pydot.graph_from_dot_file(dot_filename)

    # Создание словаря узлов для быстрого доступа
    nodes = {node.get_name().strip('"'): node for node in graph.get_nodes()}
    edges = graph.get_edges()

    # Окрашивание узлов и рёбер в красный
    for node_name in not_proven_methods:
        node_name = node_name.strip('"')
        if node_name in nodes.keys():
            set_color(nodes[node_name], "#ffccc7")
            ancestors = find_ancestors(edges, node_name)
            for anc in ancestors:
                if anc in nodes.keys():
                    set_color(nodes[anc], "#ffccc7")

    # Окрашивание узлов и рёбер в зеленый
    for node_name in poc_methods:
        node_name = node_name.strip('"')
        if node_name in nodes.keys():
            set_color(nodes[node_name], "#bae0ff")
            descendants = find_descendants(edges, node_name)
            for desc in descendants:
                if desc in nodes.keys():
                    current_color = nodes[desc].get_attributes().get('fillcolor')
                    if current_color in ["#ffccc7", None]:  # Зеленый перекрашивает красный и неокрашенные
                        set_color(nodes[desc], "#d9f7be")

    # Окрашивание узлов в синий
    for node_name in poc_methods:
        node_name = node_name.strip('"')
        if node_name in nodes.keys():
            current_color = nodes[node_name].get_attributes().get('fillcolor')
            if current_color in ["#ffccc7", "#d9f7be", None]:  # Синий перекрашивает красный, зеленый и неокрашенные
                set_color(nodes[node_name], "#bae0ff")

    # Окрашивание абстрактных узлов в желтый
    for node_name in abstract_methods:
        node_name = node_name.strip('"')
        if node_name in nodes.keys():
            set_color(nodes[node_name], "#fff1b8")  # Желтый перекрашивает все

    # Окрашивание узлов в желтый, если узел не закрашен и его имя содержит "abstract"
    for node_name, node in nodes.items():
        if "abstract" in node_name.lower() and node.get_attributes().get('fillcolor') is None:
            set_color(node, "#fff1b8")

    # Удаление красных узлов и связанных с ними рёбер
    nodes_to_delete = [node_name for node_name, node in nodes.items() if
                       node.get_attributes().get('fillcolor') == "#ffccc7"]
    for node_name in nodes_to_delete:
        graph.del_node(nodes[node_name])

    # Удаление рёбер, которые связаны с удаленными узлами
    edges_to_delete = []
    for edge in edges:
        source = edge.get_source().strip('"')
        destination = edge.get_destination().strip('"')
        if source in nodes_to_delete or destination in nodes_to_delete:
            edges_to_delete.append(edge)

    for edge in edges_to_delete:
        graph.del_edge(edge.get_source(), edge.get_destination())

    # Обновить словарь узлов после удаления
    nodes = {node.get_name().strip('"'): node for node in graph.get_nodes()}

    # Окрашивание узлов в контрастный розовый, если их имя начинается на "java.lang."
    for node_name, node in nodes.items():
        if node_name.startswith("java.lang."):
            set_color(node, "#ffd6e7")
            ancestors = find_ancestors(edges, node_name)
            for anc in ancestors:
                if anc in nodes.keys():
                    set_color(nodes[anc], "#ffd6e7")

    for node_name, node in nodes.items():
        if node_name.startswith("java.lang."):
            set_color(node, "#ff69b4")  # Контрастный розовый

    # Сохранение нового графа в формате .dot
    graph.write(output_dot_filename)

    # Сохранение нового графа в формате .svg
    graph.write_svg(output_svg_filename)


color_graph(
    dot_filename='graphs/graph_in_old.dot',
    poc_file='methods/poc_methods.txt',
    not_proven_file='methods/not_poc_methods.txt',
    abstract_file='methods/abstract_methods.txt',
    output_dot_filename='graphs/graph_out_old.dot',
    output_svg_filename='svg/graph_out_old.svg'
)
