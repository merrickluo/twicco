import React from 'react'
import Icon from 'react-native-vector-icons/MaterialCommunityIcons'
import { TouchableOpacity } from 'react-native'

export default class ImageButton extends React.Component {
  render() {
    return (
      <TouchableOpacity onPress={this.props.onPress}>
        <Icon
          name={this.props.name}
          color="white"
          size={24}
          style={styles.icon}
        />
      </TouchableOpacity>
    )
  }
}

const styles = {
  icon: {
    marginLeft: 8,
    marginRight: 8,
  }
}
