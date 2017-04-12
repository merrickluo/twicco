import React from 'react'
import Icon from 'react-native-vector-icons/MaterialCommunityIcons'

export default class ImageButton extends React.Component {
  render() {
    return (
      <Icon
        name={this.props.name}
        color="white"
        size={24}
        style={styles.icon}
      />
    )
  }
}

const styles = {
  icon: {
    marginLeft: 8,
    marginRight: 8,
  }
}
